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
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionService;
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
    void testCreateAdoption() throws IllegalOperationException, EntityNotFoundException {
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
        assertEquals("ADOPTED", entity.getPet().getStatus());
    }

    @Test
    void testCreateAdoptionPetNotAvailable() {
    assertThrows(IllegalOperationException.class, () -> {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        pet.setStatus("ADOPTED");
        entityManager.persist(pet);

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        AdoptionEntity entity = new AdoptionEntity();
        entity.setPet(pet);
        entity.setAdopter(adopter);
        entity.setAdoptionDate(LocalDate.now());
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