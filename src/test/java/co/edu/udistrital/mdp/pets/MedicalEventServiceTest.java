package co.edu.udistrital.mdp.pets;


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

import co.edu.udistrital.mdp.pets.services.MedicalEventService;
import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
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

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            MedicalEventEntity event = factory.manufacturePojo(MedicalEventEntity.class);
            event.setPet(pet);
            event.setEventType("CHECKUP");

            entityManager.persist(event);
            eventList.add(event);
        }
    }

    @Test
    void testCreateMedicalEvent() throws Exception {
        MedicalEventEntity entity = factory.manufacturePojo(MedicalEventEntity.class);
        entity.setPet(petList.get(0));
        entity.setEventType("SURGERY");

        MedicalEventEntity result = service.createMedicalEvent(entity);

        assertNotNull(result);
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
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testGetInvalidMedicalEvent() {
        assertThrows(EntityNotFoundException.class, () -> {
            service.getMedicalEvent(0L);
        });
    }

    @Test
    void testDeleteMedicalEvent() throws Exception {
        MedicalEventEntity entity = eventList.get(0);

        service.deleteMedicalEvent(entity.getId());

        MedicalEventEntity deleted = entityManager.find(MedicalEventEntity.class, entity.getId());
        assertNull(deleted);
    }
}
