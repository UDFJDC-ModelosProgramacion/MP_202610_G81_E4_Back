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

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PetService.class)
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private List<PetEntity> petList = new ArrayList<>();
    private ShelterEntity shelterEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.flush();
    }

    private void insertData() {
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);

        for (int i = 0; i < 3; i++) {
            PetEntity entity = factory.manufacturePojo(PetEntity.class);
            entity.setShelter(shelterEntity);
            entity.setVaccinationRecords(new ArrayList<>());
            entityManager.persist(entity);
            petList.add(entity);
        }
        entityManager.flush();
    }

    @Test
    void testCreatePet() throws IllegalOperationException {
        PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
        newEntity.setShelter(shelterEntity);
        newEntity.setStatus("AVAILABLE");

        PetEntity result = petService.createPet(newEntity);

        assertNotNull(result);
        PetEntity found = entityManager.find(PetEntity.class, result.getId());
        assertEquals(newEntity.getName(), found.getName());
        assertEquals(shelterEntity.getId(), found.getShelter().getId());
    }

    @Test
    void testCreatePetInvalidShelter() {
        PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
        newEntity.setShelter(null);

        assertThrows(IllegalOperationException.class, () -> {
            petService.createPet(newEntity);
        });
    }

    @Test
    void testGetPets() {
        List<PetEntity> list = petService.getPets();
        assertEquals(petList.size(), list.size());
    }

    @Test
    void testGetPet() throws EntityNotFoundException {
        PetEntity entity = petList.get(0);
        PetEntity result = petService.getPet(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void testGetPetInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            petService.getPet(999L);
        });
    }

    @Test
    void testUpdatePet() throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = petList.get(0);
        PetEntity updateData = factory.manufacturePojo(PetEntity.class);
        updateData.setName("Nombre Actualizado");
        updateData.setShelter(shelterEntity);

        PetEntity result = petService.updatePet(entity.getId(), updateData);

        assertNotNull(result);
        assertEquals("Nombre Actualizado", result.getName());
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testDeletePet() throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = petList.get(0);
        petService.deletePet(entity.getId());
        
        entityManager.flush();
        PetEntity deleted = entityManager.find(PetEntity.class, entity.getId());
        assertNull(deleted);
    }
}
