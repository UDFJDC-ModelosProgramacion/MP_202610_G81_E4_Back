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

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.ShelterService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterService.class)
public class ShelterServiceTest {

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterEntity> shelterList = new ArrayList<>();
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }
    private void insertData() {
        for (int i = 0; i < 3; i++) {

            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            shelter.setName("Shelter " + i);
            shelter.setCity("Bogotá");
            shelter.setAddress("Dirección " + i);
            shelter.setPhone("123456789");
            shelter.setEmail("test" + i + "@mail.com");
            entityManager.persist(shelter);
            shelterList.add(shelter);
        }
    }
    @Test
    void testCreateShelter() {
        ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);

        newEntity.setName("Nuevo Shelter");
        newEntity.setCity("Bogotá");
        newEntity.setAddress("Calle 123");
        newEntity.setPhone("987654321");
        newEntity.setEmail("nuevo@mail.com");

        ShelterEntity result = shelterService.createShelter(newEntity);
        assertNotNull(result);

        ShelterEntity entity = entityManager.find(ShelterEntity.class, result.getId());
        assertEquals(newEntity.getName(), entity.getName());
        assertEquals(newEntity.getCity(), entity.getCity());
        assertEquals(newEntity.getAddress(), entity.getAddress());
        assertEquals(newEntity.getPhone(), entity.getPhone());
        assertEquals(newEntity.getEmail(), entity.getEmail());
    }
    @Test
    void testCreateShelterNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterService.createShelter(null);
        });
    }
    @Test
    void testCreateShelterNoName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entity.setName("");
            shelterService.createShelter(entity);
        });
    }
    @Test
    void testCreateShelterNoCity() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entity.setCity("");
            shelterService.createShelter(entity);
        });
    }
    @Test
    void testCreateShelterNoAddress() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entity.setAddress("");
            shelterService.createShelter(entity);
        });
    }
    @Test
    void testCreateShelterNoPhone() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entity.setPhone("");
            shelterService.createShelter(entity);
        });
    }
    @Test
    void testCreateShelterNoEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entity.setEmail("");
            shelterService.createShelter(entity);
        });
    }
    @Test
    void testSearchShelter() {
        ShelterEntity entity = shelterList.get(0);

        ShelterEntity result = shelterService.searchShelter(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }
    @Test
    void testSearchShelterNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterService.searchShelter(null);
        });
    }
    @Test
    void testSearchShelterNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterService.searchShelter(0L);
        });
    }
    @Test
    void testUpdateShelter() {

        ShelterEntity entity = shelterList.get(0);
        ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);

        newEntity.setName("Actualizado");
        newEntity.setCity("Medellín");
        newEntity.setAddress("Nueva dirección");
        newEntity.setPhone("111111111");
        newEntity.setEmail("update@mail.com");

        ShelterEntity result = shelterService.updateShelter(entity.getId(), newEntity);
        assertNotNull(result);
        ShelterEntity updated = entityManager.find(ShelterEntity.class, entity.getId());
        assertEquals(newEntity.getName(), updated.getName());
        assertEquals(newEntity.getCity(), updated.getCity());
    }
    @Test
    void testUpdateShelterNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterService.updateShelter(1L, null);
        });
    }
    @Test
    void testDeleteShelter() {

        ShelterEntity entity = shelterList.get(0);

        shelterService.deleteShelter(entity.getId());

        ShelterEntity deleted = entityManager.find(ShelterEntity.class, entity.getId());

        assertNull(deleted);
    }
    @Test
    void testDeleteShelterWithPets() {

        ShelterEntity shelter = shelterList.get(0);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);

        entityManager.persist(pet);
        shelter.getPets().add(pet);

        assertThrows(IllegalStateException.class, () -> {
            shelterService.deleteShelter(shelter.getId());
        });
    }
}
    

