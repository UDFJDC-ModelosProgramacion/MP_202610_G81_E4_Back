package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VeterinarianService.class)
public class VeterinarianServiceTest {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<VeterinarianEntity> veterinarianList = new ArrayList<>();
    private ShelterEntity commonShelter;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        commonShelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(commonShelter);

        for (int i = 0; i < 3; i++) {
            VeterinarianEntity vet = factory.manufacturePojo(VeterinarianEntity.class);
            vet.setShelter(commonShelter);
            vet.setVeterinarianIdBusiness(1000L + i);
            vet.setSpecialties(Arrays.asList("General"));
            
            entityManager.persist(vet);
            veterinarianList.add(vet);
        }
    }

    @Test
    void testCreateVeterinarian() {
        VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
        newEntity.setShelter(commonShelter);
        newEntity.setVeterinarianIdBusiness(9999L);
        newEntity.setSpecialties(Arrays.asList("Surgery", "Cardiology"));

        VeterinarianEntity result = veterinarianService.createVeterinarian(newEntity);
        
        assertNotNull(result);
        VeterinarianEntity found = entityManager.find(VeterinarianEntity.class, result.getId());
        assertEquals(newEntity.getVeterinarianIdBusiness(), found.getVeterinarianIdBusiness());
        assertTrue(found.getSpecialties().contains("Surgery"));
    }

    @Test
    void testCreateVeterinarianInvalidSpecialty() {
        VeterinarianEntity entity = factory.manufacturePojo(VeterinarianEntity.class);
        entity.setShelter(commonShelter);
        entity.setSpecialties(Arrays.asList("Neurology"));
        assertThrows(IllegalArgumentException.class, () -> {
            veterinarianService.createVeterinarian(entity);
        });
    }

    @Test
    void testCreateVeterinarianNoShelter() {
        VeterinarianEntity entity = factory.manufacturePojo(VeterinarianEntity.class);
        entity.setShelter(null);
        assertThrows(IllegalArgumentException.class, () -> {
            veterinarianService.createVeterinarian(entity);
        });
    }

    @Test
    void testSearchVeterinarian() {
        VeterinarianEntity entity = veterinarianList.get(0);
        VeterinarianEntity result = veterinarianService.searchVeterinarian(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getVeterinarianIdBusiness(), result.getVeterinarianIdBusiness());
    }

    @Test
    void testSearchVeterinarianNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            veterinarianService.searchVeterinarian(0L);
        });
    }

    @Test
    void testUpdateVeterinarian() {
        VeterinarianEntity entity = veterinarianList.get(0);
        VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
        
        newEntity.setLastName("Nuevo Apellido");
        newEntity.setVeterinarianIdBusiness(entity.getVeterinarianIdBusiness());
        newEntity.setSpecialties(Arrays.asList("Dentistry"));
        newEntity.setShelter(commonShelter);

        VeterinarianEntity result = veterinarianService.updateVeterinarian(entity.getId(), newEntity);
        
        assertNotNull(result);
        VeterinarianEntity updated = entityManager.find(VeterinarianEntity.class, entity.getId());
        assertEquals("Nuevo Apellido", updated.getLastName());
        assertTrue(updated.getSpecialties().contains("Dentistry"));
    }

    @Test
    void testUpdateVeterinarianChangeIdFails() {
        VeterinarianEntity entity = veterinarianList.get(0);
        VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
        newEntity.setVeterinarianIdBusiness(8888L);
        Long entityId = entity.getId();
        assertThrows(IllegalArgumentException.class, () -> {
            veterinarianService.updateVeterinarian(entityId, newEntity);
        });
    }

    @Test
    void testDeleteVeterinarianSuccess() {
        VeterinarianEntity entity = veterinarianList.get(0);
        veterinarianService.deleteVeterinarian(entity.getId());

        VeterinarianEntity deleted = entityManager.find(VeterinarianEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteVeterinarianWithAdoptionsFails() {
        VeterinarianEntity entity = veterinarianList.get(0);
        
        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setVeterinarian(entity);
        entityManager.persist(adoption);
        entityManager.flush();
        Long entityId = entity.getId();
        assertThrows(IllegalStateException.class, () -> {
            veterinarianService.deleteVeterinarian(entityId);
        });
    }
}