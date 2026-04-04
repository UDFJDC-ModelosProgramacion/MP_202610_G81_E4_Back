package co.edu.udistrital.mdp.ZZZ.services;

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
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.flush();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            shelter.setId(null); 
            entityManager.persist(shelter);
            
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setId(null);
            pet.setShelter(shelter);
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            adopter.setId(null);
            entityManager.persist(adopter);
    
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setId(null);
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            entityManager.persist(adoption);

            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setId(null);
            review.setAdoption(adoption);
            review.setAdopter(adopter);
            review.setRating(5); 
            review.setReviewDate(LocalDateTime.now().toLocalDate());
            
            entityManager.persist(review);
            reviewList.add(review);
        }
        entityManager.flush();
    }

    @Test
    void testCreateReview() {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        adopter.setId(null);
        entityManager.persist(adopter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setId(null);
        entityManager.persist(pet);

        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setId(null);
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        entityManager.persist(adoption);

        ReviewEntity newReview = factory.manufacturePojo(ReviewEntity.class);
        newReview.setId(null);
        newReview.setAdopter(adopter);
        newReview.setAdoption(adoption);
        newReview.setRating(4); 
        newReview.setReviewDate(LocalDateTime.now().toLocalDate());

        ReviewEntity result = reviewService.createReview(newReview);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(4, result.getRating());
    }

    @Test
    void testCreateReviewInvalidRating() {
        ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
        review.setRating(10); 
        
        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(review);
        });
    }

    @Test
    void testSearchReview() {
        ReviewEntity expected = reviewList.get(0);
        ReviewEntity result = reviewService.searchReview(expected.getId());
        
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.searchReview(99999L);
        });
    }

    @Test
    void testUpdateReview() {
        ReviewEntity original = reviewList.get(0);
        ReviewEntity changes = new ReviewEntity();
        changes.setComments("Updated Comment");
        changes.setRating(2);
        changes.setReviewDate(LocalDateTime.now().toLocalDate());

        ReviewEntity updated = reviewService.updateReview(original.getId(), changes);
        
        assertNotNull(updated);
        entityManager.flush();
        entityManager.clear();
        
        ReviewEntity dbReview = entityManager.find(ReviewEntity.class, original.getId());
        assertEquals("Updated Comment", dbReview.getComments());
        assertEquals(2, dbReview.getRating());
    }

    @Test
    void testDeleteReviewByOwner() {
        ReviewEntity review = reviewList.get(0);
        Long id = review.getId();
        Long ownerId = review.getAdopter().getId();

        reviewService.deleteReview(id, ownerId, false);
        
        entityManager.flush();
        ReviewEntity deleted = entityManager.find(ReviewEntity.class, id);
        assertNull(deleted);
    }

    @Test
    void testDeleteReviewByAdmin() {
        ReviewEntity review = reviewList.get(0);
        Long id = review.getId();

        reviewService.deleteReview(id, null, true);
        
        entityManager.flush();
        assertNull(entityManager.find(ReviewEntity.class, id));
    }

    @Test
    void testDeleteReviewUnauthorized() {
        ReviewEntity review = reviewList.get(0);
        Long wrongAdopterId = review.getAdopter().getId() + 999L; 

        assertThrows(IllegalStateException.class, () -> {
            reviewService.deleteReview(review.getId(), wrongAdopterId, false);
        });
    }
}