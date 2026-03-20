package co.test.java.co.edu.udistrital.test.java.co.edu.udistrital.mdp.pets.test.java.co.edu.udistrital.mdp;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.ReviewService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReviewService.class)
public class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<ReviewEntity> reviewList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }
    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            shelter.setName("Shelter " + i);
            shelter.setCity("Bogotá");
            shelter.setAddress("Dir " + i);
            shelter.setPhone("123456");
            shelter.setEmail("test" + i + "@mail.com");
            entityManager.persist(shelter);
            
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);
    
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            entityManager.persist(adoption);

            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setAdoption(adoption);
            review.setAdopter(adopter);
            review.setRating(5);
            review.setReviewDate(LocalDateTime.now());
            entityManager.persist(review);
            reviewList.add(review);
        }
    }
    @Test
    void testCreateReview() {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(pet);

        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        entityManager.persist(adoption);

        ReviewEntity newReview = factory.manufacturePojo(ReviewEntity.class);
        newReview.setAdopter(adopter);
        newReview.setAdoption(adoption);
        newReview.setRating(4);
        newReview.setReviewDate(LocalDateTime.now());
        ReviewEntity result = reviewService.createReview(newReview);
        assertNotNull(result);
        ReviewEntity entity = entityManager.find(ReviewEntity.class, result.getId());
        assertEquals(newReview.getRating(), entity.getRating());
    }
    @Test
    void testCreateReviewInvalidRating() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setRating(10);
            reviewService.createReview(review);
        });
    }
    @Test
    void testCreateReviewNoAdoption() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setRating(4);
            review.setAdoption(null);
            reviewService.createReview(review);
        });
    }
    @Test
    void testCreateReviewNoAdopter() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setRating(4);
            review.setAdopter(null);
            reviewService.createReview(review);
        });
    }
    @Test
    void testSearchReview() {
        ReviewEntity entity = reviewList.get(0);
        ReviewEntity result = reviewService.searchReview(entity.getId());
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }
    @Test
    void testSearchReviewNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.searchReview(null);
        });
    }
    @Test
    void testSearchReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.searchReview(0L);
        });
    }
    @Test
    void testUpdateReview() {
        ReviewEntity entity = reviewList.get(0);
        ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
        newEntity.setComments("Comentario actualizado");
        ReviewEntity result = reviewService.updateReview(entity.getId(), newEntity);
        assertNotNull(result);
        ReviewEntity updated = entityManager.find(ReviewEntity.class, entity.getId());
        assertEquals("Comentario actualizado", updated.getComments());
    }
    @Test
    void testUpdateReviewNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.updateReview(1L, null);
        });
    }
    @Test
    void testDeleteReviewByOwner() {
        ReviewEntity review = reviewList.get(0);
        reviewService.deleteReview(review.getId(), review.getAdopter().getId(), false);
        ReviewEntity deleted = entityManager.find(ReviewEntity.class, review.getId());
        assertNull(deleted);
    }
    @Test
    void testDeleteReviewByAdmin() {

        ReviewEntity review = reviewList.get(0);

        reviewService.deleteReview(review.getId(), null, true);

        ReviewEntity deleted = entityManager.find(ReviewEntity.class, review.getId());

        assertNull(deleted);
    }
    @Test
    void testDeleteReviewUnauthorized() {

        ReviewEntity review = reviewList.get(0);

        assertThrows(IllegalStateException.class, () -> {
            reviewService.deleteReview(review.getId(), 999L, false);
        });
    }  
}
