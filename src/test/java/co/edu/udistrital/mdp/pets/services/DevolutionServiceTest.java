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
import co.edu.udistrital.mdp.pets.services.DevolutionService;
import jakarta.persistence.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(DevolutionService.class)
public class DevolutionServiceTest {

    @Autowired
    private DevolutionService devolutionService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private List<DevolutionEntity> devolutionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from DevolutionEntity").executeUpdate();
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

            DevolutionEntity devolution = factory.manufacturePojo(DevolutionEntity.class);
            devolution.setAdoption(adoption);
            devolution.setReturnDate(LocalDate.now().minusDays(i));
            
            entityManager.persist(devolution);
            devolutionList.add(devolution);
        }
        entityManager.flush();
    }

    @Test
    void testCreateDevolution() {
        // Arrange
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        entityManager.persist(adoption);

        DevolutionEntity newDevolution = new DevolutionEntity();
        newDevolution.setAdoption(adoption);
        newDevolution.setReturnDate(LocalDate.now());
        newDevolution.setReason("Allergy");
        newDevolution.setDetailedDescription("The owner developed a severe allergy.");
        newDevolution.setPetState("Excellent");

        // Act
        DevolutionEntity result = devolutionService.create(newDevolution);

        // Assert
        assertNotNull(result);
        DevolutionEntity entity = entityManager.find(DevolutionEntity.class, result.getId());
        assertEquals("Allergy", entity.getReason());
        assertEquals(adoption.getId(), entity.getAdoption().getId());
    }

    @Test
    void testCreateDevolutionNoAdoption() {
        assertThrows(IllegalArgumentException.class, () -> {
            DevolutionEntity devolution = new DevolutionEntity();
            devolution.setAdoption(null);
            devolutionService.create(devolution);
        });
    }

    @Test
    void testFindAllDevolutions() {
        List<DevolutionEntity> list = devolutionService.findAll();
        assertEquals(devolutionList.size(), list.size());
    }

    @Test
    void testFindDevolutionById() {
        DevolutionEntity entity = devolutionList.get(0);
        DevolutionEntity result = devolutionService.findById(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getReason(), result.getReason());
    }

    @Test
    void testFindDevolutionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            devolutionService.findById(999L);
        });
    }

    @Test
    void testUpdateDevolution() {
        // Arrange
        DevolutionEntity entity = devolutionList.get(0);
        DevolutionEntity updateData = new DevolutionEntity();
        updateData.setReturnDate(LocalDate.now());
        updateData.setReason("Updated Reason");
        updateData.setDetailedDescription("New description for test");
        updateData.setPetState("Good");

        // Act
        DevolutionEntity result = devolutionService.update(entity.getId(), updateData);

        // Assert
        assertNotNull(result);
        DevolutionEntity updated = entityManager.find(DevolutionEntity.class, entity.getId());
        assertEquals("Updated Reason", updated.getReason());
        assertEquals("Good", updated.getPetState());
    }

    @Test
    void testUpdateDevolutionNoDate() {
        DevolutionEntity entity = devolutionList.get(0);
        assertThrows(IllegalArgumentException.class, () -> {
            DevolutionEntity invalidData = new DevolutionEntity();
            invalidData.setReturnDate(null); // Esto debe disparar el error en el Service
            devolutionService.update(entity.getId(), invalidData);
        });
    }

    @Test
    void testDeleteDevolution() {
        DevolutionEntity devolution = devolutionList.get(0);
        Long id = devolution.getId();
        
        devolutionService.delete(id);
        entityManager.flush();

        DevolutionEntity deleted = entityManager.find(DevolutionEntity.class, id);
        assertNull(deleted);
    }
}