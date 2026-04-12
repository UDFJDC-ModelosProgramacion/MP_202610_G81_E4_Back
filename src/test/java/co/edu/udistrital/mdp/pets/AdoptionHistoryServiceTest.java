package co.edu.udistrital.mdp.pets;

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
import co.edu.udistrital.mdp.pets.services.AdoptionHistoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionHistoryService.class)
public class AdoptionHistoryServiceTest {

    @Autowired
    private AdoptionHistoryService historyService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionHistoryEntity> historyList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionHistoryEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);

            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            entityManager.persist(adoption);

            AdoptionHistoryEntity history = factory.manufacturePojo(AdoptionHistoryEntity.class);
            history.setAdoption(adoption);
            history.setReason("Reason " + i);
            history.setDate(LocalDate.now());
            
            entityManager.persist(history);
            historyList.add(history);
        }
    }

    @Test
    void testCreateAdoptionHistory() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        entityManager.persist(adoption);

        AdoptionHistoryEntity newHistory = factory.manufacturePojo(AdoptionHistoryEntity.class);
        newHistory.setAdoption(adoption);
        newHistory.setReason("Successful adoption");
        newHistory.setDate(LocalDate.now());

        AdoptionHistoryEntity result = historyService.createAdoptionHistory(newHistory);

        assertNotNull(result);
        AdoptionHistoryEntity entity = entityManager.find(AdoptionHistoryEntity.class, result.getId());
        assertEquals(newHistory.getReason(), entity.getReason());
        assertEquals(adoption.getId(), entity.getAdoption().getId());
    }

    @Test
    void testCreateAdoptionHistoryNoAdoption() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionHistoryEntity history = factory.manufacturePojo(AdoptionHistoryEntity.class);
            history.setAdoption(null);
            historyService.createAdoptionHistory(history);
        });
    }

    @Test
    void testCreateAdoptionHistoryEmptyReason() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionHistoryEntity history = factory.manufacturePojo(AdoptionHistoryEntity.class);
            history.setReason("");
            historyService.createAdoptionHistory(history);
        });
    }

    @Test
    void testGetAdoptionHistory() {
        AdoptionHistoryEntity entity = historyList.get(0);
        AdoptionHistoryEntity result = historyService.getAdoptionHistory(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testGetAdoptionHistoryNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            historyService.getAdoptionHistory(999L);
        });
    }

    @Test
    void testUpdateAdoptionHistory() {
        AdoptionHistoryEntity entity = historyList.get(0);
        AdoptionHistoryEntity updateData = factory.manufacturePojo(AdoptionHistoryEntity.class);
        updateData.setReason("Updated Reason");
        updateData.setDate(LocalDate.now().minusDays(1));

        AdoptionHistoryEntity result = historyService.updateAdoptionHistory(entity.getId(), updateData);

        assertNotNull(result);
        AdoptionHistoryEntity updated = entityManager.find(AdoptionHistoryEntity.class, entity.getId());
        assertEquals("Updated Reason", updated.getReason());
    }

    @Test
    void testDeleteAdoptionHistory() {
        AdoptionHistoryEntity history = historyList.get(0);
        Long id = history.getId();

        historyService.deleteAdoptionHistory(id);

        AdoptionHistoryEntity deleted = entityManager.find(AdoptionHistoryEntity.class, id);
        assertNull(deleted);
    }
}