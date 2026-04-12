package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicalEventService.class)
class MedicalEventServiceTest {

    @Autowired
    private MedicalEventService service;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicalEventEntity> eventList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();
    private List<VeterinarianEntity> vetList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);

            VeterinarianEntity vet = factory.manufacturePojo(VeterinarianEntity.class);
            vet.setShelter(shelter);
            entityManager.persist(vet);
            vetList.add(vet);

            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);
            petList.add(pet);

            MedicalEventEntity event = factory.manufacturePojo(MedicalEventEntity.class);
            event.setPet(pet);
            event.setVeterinarian(vet);
            event.setEventType("CHECKUP");

            entityManager.persist(event);
            eventList.add(event);
        }
        entityManager.flush();
    }

    @Test
    void testCreateMedicalEvent() throws Exception {
        MedicalEventEntity entity = factory.manufacturePojo(MedicalEventEntity.class);
        entity.setPet(petList.get(0));
        entity.setVeterinarian(vetList.get(0));
        entity.setEventType("SURGERY");

        MedicalEventEntity result = service.createMedicalEvent(entity);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("SURGERY", result.getEventType());
    }

    @Test
    void testCreateMedicalEventInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalEventEntity entity = new MedicalEventEntity();
            service.createMedicalEvent(entity);
        });
    }

    @Test
    void testGetMedicalEvents() {
        List<MedicalEventEntity> list = service.getMedicalEvents();
        assertEquals(eventList.size(), list.size());
    }

    @Test
    void testGetMedicalEvent() throws Exception {
        MedicalEventEntity entity = eventList.get(0);
        MedicalEventEntity result = service.getMedicalEvent(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getEventType(), result.getEventType());
    }

    @Test
    void testGetInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.getMedicalEvent(999L);
        });
    }

    @Test
    void testDeleteMedicalEvent() throws Exception {
        MedicalEventEntity entity = eventList.get(0);

        service.deleteMedicalEvent(entity.getId());
        entityManager.flush();

        MedicalEventEntity deleted = entityManager.find(MedicalEventEntity.class, entity.getId());
        assertNull(deleted);
    }
}