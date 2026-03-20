package co.test.java.co.edu.udistrital.test.java.co.edu.udistrital.mdp.pets.test.java.co.edu.udistrital.mdp;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterEventService.class)
public class ShelterEventServiceTest {

    @Autowired
    private ShelterEventService shelterEventService;

    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterEventEntity> eventList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            shelter.setName("Shelter " + i);
            shelter.setCity("Bogotá");
            shelter.setAddress("Dir " + i);
            shelter.setPhone("123456");
            shelter.setEmail("test" + i + "@mail.com");
            entityManager.persist(shelter);

            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setShelter(shelter);
            event.setEventDate(LocalDateTime.now().plusDays(5)); // 🔥 futuro obligatorio
            entityManager.persist(event);
            eventList.add(event);
        }
    }
    @Test
    void testCreateShelterEvent() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        shelter.setName("Shelter Test");
        shelter.setCity("Bogotá");
        shelter.setAddress("Dir");
        shelter.setPhone("123");
        shelter.setEmail("mail@test.com");
        entityManager.persist(shelter);

        ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newEvent.setShelter(shelter);
        newEvent.setEventDate(LocalDateTime.now().plusDays(3));

        ShelterEventEntity result = shelterEventService.createShelterEvent(newEvent);
        assertNotNull(result);

        ShelterEventEntity entity = entityManager.find(ShelterEventEntity.class, result.getId());
        assertEquals(newEvent.getEventDate(), entity.getEventDate());
        assertEquals(newEvent.getShelter().getId(), entity.getShelter().getId());
    }
    @Test
    void testCreateEventNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.createShelterEvent(null);
        });
    }
    @Test
    void testCreateEventNoDate() {
        assertThrows(IllegalArgumentException.class, () -> {

            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setEventDate(null);

            shelterEventService.createShelterEvent(event);
        });
    }
    @Test
    void testCreateEventPastDate() {
        assertThrows(IllegalArgumentException.class, () -> {

            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setEventDate(LocalDateTime.now().minusDays(1));

            shelterEventService.createShelterEvent(event);
        });
    }
    @Test
    void testCreateEventNoShelter() {
        assertThrows(IllegalArgumentException.class, () -> {

            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setEventDate(LocalDateTime.now().plusDays(2));
            event.setShelter(null);

            shelterEventService.createShelterEvent(event);
        });
    }
    @Test
    void testSearchEvent() {
        ShelterEventEntity entity = eventList.get(0);

        ShelterEventEntity result = shelterEventService.searchShelterEvent(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }
    @Test
    void testSearchEventNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.searchShelterEvent(null);
        });
    }
    @Test
    void testSearchEventNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterEventService.searchShelterEvent(0L);
        });
    }
    @Test
    void testUpdateEvent() {
        ShelterEventEntity entity = eventList.get(0);
        ShelterEventEntity newEntity = factory.manufacturePojo(ShelterEventEntity.class);
        newEntity.setEventDate(LocalDateTime.now().plusDays(10));

        ShelterEventEntity result = shelterEventService.updateShelterEvent(entity.getId(), newEntity);
        assertNotNull(result);
        ShelterEventEntity updated = entityManager.find(ShelterEventEntity.class, entity.getId());
        assertEquals(newEntity.getEventDate(), updated.getEventDate());
    }
    @Test
    void testUpdateEventNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.updateShelterEvent(1L, null);
        });
    }
    @Test
    void testDeleteEventFuture() {
        ShelterEventEntity entity = eventList.get(0);
        assertThrows(IllegalStateException.class, () -> {
            shelterEventService.deleteShelterEvent(entity.getId());
        });
    }
    @Test
    void testDeleteEventPast() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        shelter.setName("Shelter");
        shelter.setCity("Bogotá");
        shelter.setAddress("Dir");
        shelter.setPhone("123");
        shelter.setEmail("mail@test.com");
        entityManager.persist(shelter);

        ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
        event.setShelter(shelter);
        event.setEventDate(LocalDateTime.now().minusDays(2)); // 🔥 pasado
        entityManager.persist(event);

        shelterEventService.deleteShelterEvent(event.getId());
        ShelterEventEntity deleted = entityManager.find(ShelterEventEntity.class, event.getId());
        assertNull(deleted);
    }
}