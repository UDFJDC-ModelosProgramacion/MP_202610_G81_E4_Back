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
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import jakarta.persistence.EntityNotFoundException;
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

    private final PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionTrackingEntity> trackingList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionTrackingEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.flush();
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
        entityManager.flush();
    }

    @Test
    void testCreateAdoptionTracking() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        entityManager.persist(adoption);

        AdoptionTrackingEntity newTracking = new AdoptionTrackingEntity();
        newTracking.setAdoption(adoption);
        newTracking.setFrequency("Weekly");
        newTracking.setNextReview(LocalDate.now().plusDays(7));
        newTracking.setNotes("First visit notes");

        AdoptionTrackingEntity result = trackingService.createAdoptionTracking(newTracking);

        assertNotNull(result);
        AdoptionTrackingEntity entity = entityManager.find(AdoptionTrackingEntity.class, result.getId());
        assertEquals("Weekly", entity.getFrequency());
        assertEquals(adoption.getId(), entity.getAdoption().getId());
    }

    @Test
    void testCreateTrackingNoAdoption() {
        AdoptionTrackingEntity tracking = new AdoptionTrackingEntity();
        tracking.setAdoption(null);
        assertThrows(IllegalArgumentException.class, () -> {
            trackingService.createAdoptionTracking(tracking);
        });
    }

    @Test
    void testFindAllTrackings() {
        List<AdoptionTrackingEntity> list = trackingService.getAdoptionTrackings();
        assertEquals(trackingList.size(), list.size());
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
        AdoptionTrackingEntity updateData = new AdoptionTrackingEntity();
        updateData.setFrequency("Quarterly");
        updateData.setNextReview(LocalDate.now().plusMonths(3));
        updateData.setNotes("Updated notes for testing");

        AdoptionTrackingEntity result = trackingService.updateAdoptionTracking(entity.getId(), updateData);

        assertNotNull(result);
        AdoptionTrackingEntity updated = entityManager.find(AdoptionTrackingEntity.class, entity.getId());
        assertEquals("Quarterly", updated.getFrequency());
        assertEquals("Updated notes for testing", updated.getNotes());
    }

    @Test
    void testDeleteAdoptionTracking() {
        AdoptionTrackingEntity tracking = trackingList.get(0);
        Long id = tracking.getId();
        
        trackingService.deleteAdoptionTracking(id);
        entityManager.flush();

        AdoptionTrackingEntity deleted = entityManager.find(AdoptionTrackingEntity.class, id);
        assertNull(deleted);
    }
}