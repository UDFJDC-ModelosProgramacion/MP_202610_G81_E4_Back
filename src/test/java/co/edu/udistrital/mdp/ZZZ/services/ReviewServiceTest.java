package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
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
            entityManager.persist(shelter);

            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            adopter.setFirstName("Nombre");
            adopter.setLastName("Apellido");
            entityManager.persist(adopter);

            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            entityManager.persist(adoption);

            ReviewEntity review = new ReviewEntity();
            review.setComments("Test");
            review.setRating(5);
            review.setReviewDate(LocalDate.now());
            review.setAdopter(adopter);
            review.setAdoption(adoption);

            entityManager.persist(review);
            reviewList.add(review);
        }
        entityManager.flush();
    }

    @Test
    void testCreateReview() {

        AdopterEntity adopter = entityManager.persist(factory.manufacturePojo(AdopterEntity.class));
        adopter.setFirstName("Juan");
        adopter.setLastName("Perez");

        ShelterEntity shelter = entityManager.persist(factory.manufacturePojo(ShelterEntity.class));

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdoptionEntity adoption = new AdoptionEntity();
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        entityManager.persist(adoption);

        ReviewDTO dto = new ReviewDTO();
        dto.setComments("Nueva review");
        dto.setRating(4);
        dto.setReviewDate(LocalDate.now());
        dto.setAdopterId(adopter.getId());
        dto.setAdoptionId(adoption.getId());

        ReviewEntity result = reviewService.createReview(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(4, result.getRating());
    }

    @Test
    void testCreateReviewInvalidRating() {

        AdopterEntity adopter = entityManager.persist(factory.manufacturePojo(AdopterEntity.class));
        adopter.setFirstName("Juan");
        adopter.setLastName("Perez");

        ShelterEntity shelter = entityManager.persist(factory.manufacturePojo(ShelterEntity.class));

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdoptionEntity adoption = new AdoptionEntity();
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        entityManager.persist(adoption);

        ReviewDTO dto = new ReviewDTO();
        dto.setRating(10); // inválido
        dto.setAdopterId(adopter.getId());
        dto.setAdoptionId(adoption.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            reviewService.createReview(dto);
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

        ReviewDTO dto = new ReviewDTO();
        dto.setComments("Updated");
        dto.setRating(2);
        dto.setReviewDate(LocalDate.now());

        ReviewEntity updated = reviewService.updateReview(original.getId(), dto);

        assertNotNull(updated);

        entityManager.flush();
        entityManager.clear();

        ReviewEntity db = entityManager.find(ReviewEntity.class, original.getId());
        assertEquals("Updated", db.getComments());
        assertEquals(2, db.getRating());
    }

    @Test
    void testDeleteReviewByOwner() {
        ReviewEntity review = reviewList.get(0);

        reviewService.deleteReview(review.getId(), review.getAdopter().getId(), false);

        entityManager.flush();
        assertNull(entityManager.find(ReviewEntity.class, review.getId()));
    }

    @Test
    void testDeleteReviewByAdmin() {
        ReviewEntity review = reviewList.get(0);

        reviewService.deleteReview(review.getId(), null, true);

        entityManager.flush();
        assertNull(entityManager.find(ReviewEntity.class, review.getId()));
    }

    @Test
    void testDeleteReviewUnauthorized() {
        ReviewEntity review = reviewList.get(0);

        assertThrows(IllegalStateException.class, () -> {
            reviewService.deleteReview(review.getId(), 999L, false);
        });
    }
}