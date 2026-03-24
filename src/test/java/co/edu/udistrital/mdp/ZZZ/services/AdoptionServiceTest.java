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
import co.edu.udistrital.mdp.pets.services.AdoptionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionService.class)
public class AdoptionServiceTest {
    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionEntity> adoptionList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
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
            pet.setStatus("AVAILABLE"); 
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);

            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            adoption.setAdoptionDate(LocalDate.now());
            adoption.setStatus("IN_PROGRESS");

            entityManager.persist(adoption);
            adoptionList.add(adoption);
        }
    }
    @Test
    void testCreateAdoption() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        shelter.setName("Shelter");
        shelter.setCity("Bogotá");
        shelter.setAddress("Dir");
        shelter.setPhone("123");
        shelter.setEmail("mail@test.com");
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        pet.setStatus("AVAILABLE"); 
        entityManager.persist(pet);

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        AdoptionEntity newEntity = factory.manufacturePojo(AdoptionEntity.class);
        newEntity.setPet(pet);
        newEntity.setAdopter(adopter);
        newEntity.setAdoptionDate(LocalDate.now());

        AdoptionEntity result = adoptionService.createAdoption(newEntity);
        assertNotNull(result);
        AdoptionEntity entity = entityManager.find(AdoptionEntity.class, result.getId());
        assertEquals(newEntity.getAdoptionDate(), entity.getAdoptionDate());
    }
    @Test
    void testCreateAdoptionNoPet() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionEntity entity = factory.manufacturePojo(AdoptionEntity.class);
            entity.setPet(null);
            adoptionService.createAdoption(entity);
        });
    }
    @Test
    void testCreateAdoptionPetNotAvailable() {
        assertThrows(IllegalStateException.class, () -> {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setStatus("ADOPTED"); 
            entityManager.persist(pet);
            AdoptionEntity entity = factory.manufacturePojo(AdoptionEntity.class);
            entity.setPet(pet);
            entity.setAdoptionDate(LocalDate.now());
            adoptionService.createAdoption(entity);
        });
    }
    @Test
    void testCreateAdoptionNoDate() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdoptionEntity entity = factory.manufacturePojo(AdoptionEntity.class);
            entity.setAdoptionDate(null);
            adoptionService.createAdoption(entity);
        });
    }
    @Test
    void testSearchAdoption() {
        AdoptionEntity entity = adoptionList.get(0);
        AdoptionEntity result = adoptionService.searchAdoption(entity.getId());
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }
    @Test
    void testSearchAdoptionNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.searchAdoption(null);
        });
    }
    @Test
    void testSearchAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adoptionService.searchAdoption(0L);
        });
    }
    @Test
    void testUpdateAdoption() {
        AdoptionEntity entity = adoptionList.get(0);
        AdoptionEntity newEntity = factory.manufacturePojo(AdoptionEntity.class);
        newEntity.setStatus("FINISHED");
        AdoptionEntity result = adoptionService.updateAdoption(entity.getId(), newEntity);
        assertNotNull(result);

        AdoptionEntity updated = entityManager.find(AdoptionEntity.class, entity.getId());
        assertEquals("FINISHED", updated.getStatus());
    }
    @Test
    void testUpdateAdoptionNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            adoptionService.updateAdoption(1L, null);
        });
    }
    @Test
    void testDeleteAdoptionNotFinished() {
        AdoptionEntity entity = adoptionList.get(0);
        assertThrows(IllegalStateException.class, () -> {
            adoptionService.deleteAdoption(entity.getId());
        });
    }
    @Test
    void testDeleteAdoptionFinished() {
        AdoptionEntity entity = adoptionList.get(0);
        entity.setStatus("FINISHED");
        entityManager.persist(entity);
        adoptionService.deleteAdoption(entity.getId());

        AdoptionEntity deleted = entityManager.find(AdoptionEntity.class, entity.getId());
        assertNull(deleted);
    }
}