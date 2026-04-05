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
import co.edu.udistrital.mdp.pets.services.AdopterService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdopterService.class)
public class AdopterServiceTest {

    @Autowired
    private AdopterService adopterService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdopterEntity> adopterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            adopter.setAdopterIdBusiness(1000L + i);
            adopter.setHousingType("Casa");
            adopter.setLastName("Apellido " + i);
            
            entityManager.persist(adopter);
            adopterList.add(adopter);
        }
    }

    @Test
    void testCreateAdopter() {
        AdopterEntity newEntity = factory.manufacturePojo(AdopterEntity.class);
        newEntity.setAdopterIdBusiness(9999L);
        newEntity.setHousingType("Apartamento");

        AdopterEntity result = adopterService.createAdopter(newEntity);
        
        assertNotNull(result);
        AdopterEntity found = entityManager.find(AdopterEntity.class, result.getId());
        assertEquals(newEntity.getAdopterIdBusiness(), found.getAdopterIdBusiness());
        assertEquals("Apartamento", found.getHousingType());
    }

    @Test
    void testCreateAdopterInvalidHousing() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdopterEntity newEntity = factory.manufacturePojo(AdopterEntity.class);
            newEntity.setHousingType("Hotel"); // Tipo no permitido
            adopterService.createAdopter(newEntity);
        });
    }

    @Test
    void testCreateAdopterDuplicateBusinessId() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdopterEntity existing = adopterList.get(0);
            AdopterEntity newEntity = factory.manufacturePojo(AdopterEntity.class);
            newEntity.setAdopterIdBusiness(existing.getAdopterIdBusiness());
            newEntity.setHousingType("Casa");
            adopterService.createAdopter(newEntity);
        });
    }

    @Test
    void testSearchAdopter() {
        AdopterEntity entity = adopterList.get(0);
        AdopterEntity result = adopterService.searchAdopter(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getAdopterIdBusiness(), result.getAdopterIdBusiness());
    }

    @Test
    void testSearchAdopterNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adopterService.searchAdopter(0L);
        });
    }

    @Test
    void testUpdateAdopter() {
        AdopterEntity entity = adopterList.get(0);
        AdopterEntity pojo = factory.manufacturePojo(AdopterEntity.class);
        
        pojo.setLastName("Nuevo Apellido");
        pojo.setHousingType("Finca");
        pojo.setAdopterIdBusiness(entity.getAdopterIdBusiness());

        AdopterEntity result = adopterService.updateAdopter(entity.getId(), pojo);
        
        assertNotNull(result);
        AdopterEntity updated = entityManager.find(AdopterEntity.class, entity.getId());
        assertEquals("Nuevo Apellido", updated.getLastName());
        assertEquals("Finca", updated.getHousingType());
    }

    @Test
    void testDeleteAdopterSuccess() {
        AdopterEntity entity = adopterList.get(0);
        // El adoptante no tiene solicitudes ni adopciones en el setup inicial
        adopterService.deleteAdopter(entity.getId());

        AdopterEntity deleted = entityManager.find(AdopterEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteAdopterWithRequests() {
        AdopterEntity entity = adopterList.get(0);
        
        // Simular una solicitud de adopción vinculada
        AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
        request.setAdopter(entity);
        entityManager.persist(request);
        
        // Forzar flush para que el conteo en el service detecte la relación
        entityManager.flush();

        assertThrows(IllegalStateException.class, () -> {
            adopterService.deleteAdopter(entity.getId());
        });
    }
}
