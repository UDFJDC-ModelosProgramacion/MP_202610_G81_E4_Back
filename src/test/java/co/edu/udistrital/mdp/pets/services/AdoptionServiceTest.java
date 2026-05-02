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
    void testCreateAdoptionWithNullPet(){
        ShelterEntity shelter = TestEntityFactory.createShelter(factory);
        entityManager.persist(shelter);
        PetEntity pet = null;
        AdopterEntity adopter = TestEntityFactory.createAdopter(factory);
        entityManager.persist(adopter);
        AdoptionEntity newEntity = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");
        
        assertThrows(IllegalOperationException.class, () -> {
            adoptionService.createAdoption(newEntity);
        });

    }
    @Test
    void testCreateAdoptionWithNullAdopter(){
        ShelterEntity shelter = TestEntityFactory.createShelter(factory);
        entityManager.persist(shelter);
        PetEntity pet = TestEntityFactory.createPet(factory, shelter, "AVAILABLE");
        entityManager.persist(pet);
        AdopterEntity adopter = null;
        AdoptionEntity newEntity = TestEntityFactory.createAdoption(pet, adopter, "IN_PROGRESS");
        
        assertThrows(IllegalOperationException.class, () -> {
            adoptionService.createAdoption(newEntity);
        });
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
    void searchAdoptions(){
        List<AdoptionEntity> list = adoptionService.searchAdoptions();
        assertEquals(adoptionList.size(), list.size());
    }
    @Test
    void updateAdoption() throws Exception{
        AdoptionEntity entity = adoptionList.get(0);
        AdoptionEntity updateData = factory.manufacturePojo(AdoptionEntity.class);
        updateData.setStatus("IN_PROGRESS");
        updateData.setAdoptionDate(LocalDate.now());
        AdoptionEntity result = adoptionService.updateAdoption(entity.getId(), updateData);
        assertNotNull(result);
        AdoptionEntity updated = entityManager.find(AdoptionEntity.class, entity.getId());
        assertEquals("IN_PROGRESS", updated.getStatus());
        assertEquals(LocalDate.now(), updated.getAdoptionDate());

    }
    @Test
    void testUpdateAdoptionInvalid(){
        AdoptionEntity adoptionEntity = adoptionList.get(0);
        assertThrows(EntityNotFoundException.class, () ->{
            adoptionService.updateAdoption(999L, adoptionEntity);
        });
    }
    @Test
    void testUpdateAdoptionFinishedAdoption(){
        AdoptionEntity entity = adoptionList.get(0);
        entity.setStatus("FINISHED");
        AdoptionEntity updateData = factory.manufacturePojo(AdoptionEntity.class);
        updateData.setStatus("AVAILABLE");
        updateData.setAdoptionDate(LocalDate.now());
        entityManager.merge(entity);
        entityManager.flush();
        entityManager.clear();
        assertThrows(IllegalOperationException.class,()-> {
            adoptionService.updateAdoption(entity.getId(), updateData);
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
    void testDeleteAdoptionInvalid(){
        AdoptionEntity entity = adoptionList.get(0);
        entity.setStatus("AVAILABLE");
        assertThrows(IllegalOperationException.class, () ->{
            adoptionService.deleteAdoption(entity.getId());
        });
    }
    @Test
    void testSearchAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adoptionService.searchAdoption(999L);
        });
    }
}