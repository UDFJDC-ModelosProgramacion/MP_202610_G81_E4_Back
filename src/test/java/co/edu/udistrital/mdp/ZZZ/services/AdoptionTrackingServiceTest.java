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

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionTrackingService.class)
public class AdoptionTrackingServiceTest {

    @Autowired
    private AdoptionTrackingService trackingService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionTrackingEntity> trackingList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        // Borrar en orden para respetar llaves foráneas
        entityManager.getEntityManager().createQuery("delete from AdoptionTrackingEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);

            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(pet);
            entityManager.persist(adoption);

            AdoptionTrackingEntity tracking = factory.manufacturePojo(AdoptionTrackingEntity.class);
            tracking.setAdoption(adoption);
            tracking.setFrequency("Monthly");
            tracking.setNextReview(LocalDate.now().plusMonths(1));
            
            entityManager.persist(tracking);
            trackingList.add(tracking);
        }
    }

    @Test
    void testCreateAdoptionTracking() {
        // Arrange
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        entityManager.persist(adoption);

        AdoptionTrackingEntity newTracking = factory.manufacturePojo(AdoptionTrackingEntity.class);
        newTracking.setAdoption(adoption);
        newTracking.setFrequency("Weekly");
        newTracking.setNextReview(LocalDate.now().plusDays(7));

        // Act
        AdoptionTrackingEntity result = trackingService.createAdoptionTracking(newTracking);

        // Assert
        assertNotNull(result);
        AdoptionTrackingEntity entity = entityManager.find(AdoptionTrackingEntity.class, result.getId());
        assertEquals(newTracking.getFrequency(), entity.getFrequency());
        assertEquals(adoption.getId(), entity.getAdoption().getId());
    }

    @Test
    void testCreateTrackingNoAdoption() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionTrackingEntity tracking = factory.manufacturePojo(AdoptionTrackingEntity.class);
            tracking.setAdoption(null);
            trackingService.createAdoptionTracking(tracking);
        });
    }

    @Test
    void testGetAdoptionTracking() {
        AdoptionTrackingEntity entity = trackingList.get(0);
        AdoptionTrackingEntity result = trackingService.getAdoptionTracking(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testGetAdoptionTrackingNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            trackingService.getAdoptionTracking(999L);
        });
    }

    @Test
    void testUpdateAdoptionTracking() {
        AdoptionTrackingEntity entity = trackingList.get(0);
        AdoptionTrackingEntity updateData = factory.manufacturePojo(AdoptionTrackingEntity.class);
        updateData.setFrequency("Quarterly");
        updateData.setNextReview(LocalDate.now().plusMonths(3));
        AdoptionTrackingEntity result = trackingService.updateAdoptionTracking(entity.getId(), updateData);
        assertNotNull(result);
        AdoptionTrackingEntity updated = entityManager.find(AdoptionTrackingEntity.class, entity.getId());
        assertEquals("Quarterly", updated.getFrequency());
        assertEquals(updateData.getNextReview(), updated.getNextReview());
    }

    @Test
    void testUpdateTrackingNoDate() {
        AdoptionTrackingEntity entity = trackingList.get(0);
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionTrackingEntity invalidData = new AdoptionTrackingEntity();
            invalidData.setNextReview(null);
            trackingService.updateAdoptionTracking(entity.getId(), invalidData);
        });
    }

    @Test
    void testDeleteAdoptionTracking() {
        AdoptionTrackingEntity tracking = trackingList.get(0);
        Long id = tracking.getId();
        trackingService.deleteAdoptionTracking(id);
        AdoptionTrackingEntity deleted = entityManager.find(AdoptionTrackingEntity.class, id);
        assertNull(deleted);
    }
}