package co.edu.udistrital.mdp.pets.services;

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

import co.edu.udistrital.mdp.pets.TestEntityFactory;
import co.edu.udistrital.mdp.pets.entities.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReviewService.class)
class ReviewServiceTest {

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
            ShelterEntity shelter = TestEntityFactory.createShelter(factory);
            entityManager.persist(shelter);

            PetEntity pet = TestEntityFactory.createPet(factory, shelter, "AVAILABLE");
            entityManager.persist(pet);

            AdopterEntity adopter = TestEntityFactory.createAdopter(factory);
            entityManager.persist(adopter);

            AdoptionEntity adoption = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");
            entityManager.persist(adoption);

            ReviewEntity review = new ReviewEntity();
            review.setComments("Test " + i);
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
        AdopterEntity adopter = entityManager.persist(TestEntityFactory.createAdopter(factory));
        ShelterEntity shelter = entityManager.persist(TestEntityFactory.createShelter(factory));

        PetEntity pet = TestEntityFactory.createPet(factory, shelter, "AVAILABLE");
        entityManager.persist(pet);

        AdoptionEntity adoption = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");
        entityManager.persist(adoption);

        ReviewEntity newReview = new ReviewEntity();
        newReview.setComments("Nueva review");
        newReview.setRating(4);
        newReview.setReviewDate(LocalDate.now());
        
        // El servicio requiere review, adoptionId y adopterId
        ReviewEntity result = reviewService.createReview(newReview, adoption.getId(), adopter.getId());

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(4, result.getRating());
        assertEquals(adopter.getId(), result.getAdopter().getId());
    }

    @Test
    void testGetReview() { // CORRECCIÓN: Nombre coincidente con el Service
        ReviewEntity expected = reviewList.get(0);
        ReviewEntity result = reviewService.getReview(expected.getId());

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testGetReviewNotFound() { // CORRECCIÓN: Nombre coincidente con el Service
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.getReview(99999L);
        });
    }

    @Test
    void testGetReviews() { // Nuevo test para cubrir el método de listar
        List<ReviewEntity> list = reviewService.getReviews();
        assertEquals(reviewList.size(), list.size());
    }

    @Test
    void testUpdateReview() {
        ReviewEntity original = reviewList.get(0);

        ReviewEntity details = new ReviewEntity();
        details.setComments("Updated");
        details.setRating(2);
        details.setReviewDate(LocalDate.now());
        
        ReviewEntity updated = reviewService.updateReview(original.getId(), details);

        assertNotNull(updated);
        assertEquals("Updated", updated.getComments());
        assertEquals(2, updated.getRating());
    }

    @Test
    void testDeleteReview() {
        ReviewEntity review = reviewList.get(0);
        reviewService.deleteReview(review.getId());
        entityManager.flush();
        
        ReviewEntity deleted = entityManager.find(ReviewEntity.class, review.getId());
        assertNull(deleted);
    }
}