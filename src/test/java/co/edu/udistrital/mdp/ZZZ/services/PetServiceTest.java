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
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.PetService;
import jakarta.persistence.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PetService.class)
public class PetServiceTest {

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
        // Primero creamos un Shelter porque Pet lo requiere (nullable = false)
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);

        for (int i = 0; i < 3; i++) {
            PetEntity entity = factory.manufacturePojo(PetEntity.class);
            entity.setShelter(shelterEntity);
            entity.setStatus("AVAILABLE");
            entityManager.persist(entity);
            petList.add(entity);
        }
        entityManager.flush();
    }

    @Test
    void testCreatePet() {
        PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
        newEntity.setShelter(shelterEntity);
        newEntity.setStatus("AVAILABLE");

        PetEntity result = petService.createPet(newEntity);

        assertNotNull(result);
        PetEntity entity = entityManager.find(PetEntity.class, result.getId());
        assertEquals(newEntity.getName(), entity.getName());
        assertEquals("AVAILABLE", entity.getStatus());
    }

    @Test
    void testSearchPets() {
        List<PetEntity> list = petService.searchPets();
        assertEquals(petList.size(), list.size());
    }

    @Test
    void testSearchPet() {
        PetEntity entity = petList.get(0);
        PetEntity result = petService.searchPet(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
    }

    @Test
    void testSearchPetInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            petService.searchPet(999L);
        });
    }

    @Test
    void testUpdatePet() {
        PetEntity entity = petList.get(0);
        PetEntity updateData = factory.manufacturePojo(PetEntity.class);
        updateData.setName("Rex Actualizado");
        updateData.setStatus("ADOPTED");

        PetEntity result = petService.updatePet(entity.getId(), updateData);

        assertNotNull(result);
        assertEquals("Rex Actualizado", result.getName());
        assertEquals("ADOPTED", result.getStatus());
    }

    @Test
    void testDeletePet() {
        PetEntity entity = petList.get(0);
        petService.deletePet(entity.getId());
        
        entityManager.flush();
        PetEntity deleted = entityManager.find(PetEntity.class, entity.getId());
        assertNull(deleted);
    }
}
