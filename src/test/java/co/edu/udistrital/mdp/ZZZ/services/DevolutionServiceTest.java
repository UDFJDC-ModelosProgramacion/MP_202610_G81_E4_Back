package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.DevolutionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

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

    private PodamFactory factory = new PodamFactoryImpl();
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

            DevolutionEntity devolution = factory.manufacturePojo(DevolutionEntity.class);
            devolution.setAdoption(adoption);
            devolution.setReason("Reason " + i);
            devolution.setPetState("Healthy");
            
            entityManager.persist(devolution);
            devolutionList.add(devolution);
        }
    }

    @Test
    void testCreateDevolution() {
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        entityManager.persist(adoption);

        DevolutionEntity newDevolution = factory.manufacturePojo(DevolutionEntity.class);
        newDevolution.setAdoption(adoption);
        newDevolution.setReason("Relocation");
        newDevolution.setPetState("Good");

        DevolutionEntity result = devolutionService.createDevolution(newDevolution);

        assertNotNull(result);
        DevolutionEntity entity = entityManager.find(DevolutionEntity.class, result.getId());
        assertEquals(newDevolution.getReason(), entity.getReason());
    }

    @Test
    void testCreateDevolutionDuplicate() {
        assertThrows(IllegalArgumentException.class, () -> {
            DevolutionEntity duplicate = factory.manufacturePojo(DevolutionEntity.class);
            duplicate.setAdoption(devolutionList.get(0).getAdoption());
            devolutionService.createDevolution(duplicate);
        });
    }

    @Test
    void testCreateDevolutionNoAdoption() {
        assertThrows(IllegalArgumentException.class, () -> {
            DevolutionEntity devolution = factory.manufacturePojo(DevolutionEntity.class);
            devolution.setAdoption(null);
            devolutionService.createDevolution(devolution);
        });
    }

    @Test
    void testGetDevolution() {
        DevolutionEntity entity = devolutionList.get(0);
        DevolutionEntity result = devolutionService.getDevolution(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testGetDevolutionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            devolutionService.getDevolution(999L);
        });
    }

    @Test
    void testUpdateDevolution() {
        DevolutionEntity entity = devolutionList.get(0);
        DevolutionEntity updateData = factory.manufacturePojo(DevolutionEntity.class);
        updateData.setReason("Updated");

        DevolutionEntity result = devolutionService.updateDevolution(entity.getId(), updateData);

        assertNotNull(result);
        DevolutionEntity updated = entityManager.find(DevolutionEntity.class, entity.getId());
        assertEquals("Updated", updated.getReason());
    }

    @Test
    void testDeleteDevolution() {
        DevolutionEntity devolution = devolutionList.get(0);
        Long id = devolution.getId();

        devolutionService.deleteDevolution(id);

        DevolutionEntity deleted = entityManager.find(DevolutionEntity.class, id);
        assertNull(deleted);
    }
}