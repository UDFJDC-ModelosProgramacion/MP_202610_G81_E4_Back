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
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.AdoptionService;
import jakarta.persistence.EntityNotFoundException;
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

    private final PodamFactory factory = new PodamFactoryImpl();
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
        entityManager.flush();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
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
        entityManager.flush();
    }

    @Test
    void testCreateAdoption() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        pet.setStatus("AVAILABLE");
        entityManager.persist(pet);

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        AdoptionEntity newEntity = new AdoptionEntity();
        newEntity.setPet(pet);
        newEntity.setAdopter(adopter);
        newEntity.setAdoptionDate(LocalDate.now());
        newEntity.setStatus("IN_PROGRESS");

        AdoptionEntity result = adoptionService.createAdoption(newEntity);
        
        assertNotNull(result);
        AdoptionEntity entity = entityManager.find(AdoptionEntity.class, result.getId());
        assertEquals(newEntity.getAdoptionDate(), entity.getAdoptionDate());
    }

    @Test
    void testCreateAdoptionPetNotAvailable() {
        assertThrows(IllegalStateException.class, () -> {
            PetEntity pet = adoptionList.get(0).getPet();
            pet.setStatus("ADOPTED"); // Cambiamos a no disponible
            entityManager.merge(pet);

            AdoptionEntity entity = new AdoptionEntity();
            entity.setPet(pet);
            entity.setAdopter(adoptionList.get(0).getAdopter());
            entity.setAdoptionDate(LocalDate.now());
            adoptionService.createAdoption(entity);
        });
    }

    @Test
    void testDeleteAdoption() {
        AdoptionEntity entity = adoptionList.get(0);
        entity.setStatus("FINISHED");
        entityManager.merge(entity);
        entityManager.flush(); 
        
        adoptionService.deleteAdoption(entity.getId());
        entityManager.flush(); 

        AdoptionEntity deleted = entityManager.find(AdoptionEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testSearchAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adoptionService.searchAdoption(999L);
        });
    }
}