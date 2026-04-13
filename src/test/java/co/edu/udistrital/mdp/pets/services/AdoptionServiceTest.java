package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.TestEntityFactory;
import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionService.class)
class AdoptionServiceTest {

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
            ShelterEntity shelter = TestEntityFactory.createShelter(factory);
            entityManager.persist(shelter);

            PetEntity pet = TestEntityFactory.createPet(factory, shelter, "AVAILABLE");
            entityManager.persist(pet);

            AdopterEntity adopter = TestEntityFactory.createAdopter(factory);
            entityManager.persist(adopter);

            AdoptionEntity adoption = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");

            entityManager.persist(adoption);
            adoptionList.add(adoption);
        }
        entityManager.flush();
    }

    @Test
    void testCreateAdoption() throws IllegalOperationException, EntityNotFoundException {
        ShelterEntity shelter = TestEntityFactory.createShelter(factory);
        entityManager.persist(shelter);

        PetEntity pet = TestEntityFactory.createPet(factory, shelter, "AVAILABLE");
        entityManager.persist(pet);

        AdopterEntity adopter = TestEntityFactory.createAdopter(factory);
        entityManager.persist(adopter);

        AdoptionEntity newEntity = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");

        AdoptionEntity result = adoptionService.createAdoption(newEntity);
        
        assertNotNull(result);
        AdoptionEntity entity = entityManager.find(AdoptionEntity.class, result.getId());
        assertEquals(newEntity.getAdoptionDate(), entity.getAdoptionDate());
        assertEquals("ADOPTED", entity.getPet().getStatus());
    }

    @Test
    void testCreateAdoptionPetNotAvailable() {
    assertThrows(IllegalOperationException.class, () -> {
        ShelterEntity shelter = TestEntityFactory.createShelter(factory);
        entityManager.persist(shelter);

        PetEntity pet = TestEntityFactory.createPet(factory, shelter, "ADOPTED");
        entityManager.persist(pet);

        AdopterEntity adopter = TestEntityFactory.createAdopter(factory);
        entityManager.persist(adopter);

        AdoptionEntity entity = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");
        adoptionService.createAdoption(entity);
    });
}

    @Test
    void testDeleteAdoption() throws EntityNotFoundException, IllegalOperationException {
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