package co.edu.udistrital.mdp.pets.services;

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

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterEventService.class)
public class ShelterEventServiceTest {

    @Autowired
    private ShelterEventService eventService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterEventEntity> eventList = new ArrayList<>();
    private ShelterEntity sharedShelter;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.flush();
    }

    private void insertData() {
        // Creamos un shelter base para todos los eventos del test
        sharedShelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(sharedShelter);

        for (int i = 0; i < 3; i++) {
            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setShelter(sharedShelter);
            event.setEventDate(LocalDateTime.now().plusDays(5)); // Fecha futura para que sea válido
            
            // Importante: No seteamos eventCode manualmente si es autoincremental
            ShelterEventEntity persisted = entityManager.persistFlushFind(event);
            eventList.add(persisted);
        }
    }

    @Test
    void testCreateShelterEventSuccess() {
        ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newEvent.setShelter(sharedShelter);
        newEvent.setEventDate(LocalDateTime.now().plusDays(2)); // Futuro

        ShelterEventEntity result = eventService.createShelterEvent(newEvent);

        assertNotNull(result);
        assertNotNull(result.getEventCode());
        assertEquals(newEvent.getTitle(), result.getTitle());
    }

    @Test
    void testCreateEventPastDateThrowsException() {
        ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
        event.setShelter(sharedShelter);
        event.setEventDate(LocalDateTime.now().minusDays(1)); // PASADO: Debe fallar

        assertThrows(IllegalArgumentException.class, () -> {
            eventService.createShelterEvent(event);
        });
    }

    @Test
    void testSearchEventByCode() {
        ShelterEventEntity target = eventList.get(0);
        ShelterEventEntity result = eventService.searchShelterEventByCode(target.getEventCode());

        assertNotNull(result);
        assertEquals(target.getEventCode(), result.getEventCode());
    }

    @Test
    void testSearchEventNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.searchShelterEventByCode(9999L);
        });
    }

    @Test
    void testUpdateEventByCode() {
        ShelterEventEntity target = eventList.get(0);
        ShelterEventEntity updateData = factory.manufacturePojo(ShelterEventEntity.class);
        updateData.setEventDate(LocalDateTime.now().plusDays(10));
        updateData.setShelter(sharedShelter);

        ShelterEventEntity result = eventService.updateShelterEventByCode(target.getEventCode(), updateData);

        assertNotNull(result);
        assertEquals(updateData.getTitle(), result.getTitle());
        assertEquals(updateData.getEventDate(), result.getEventDate());
    }

    @Test
    void testDeleteFutureEventThrowsException() {
        // La lógica dice: NO se pueden borrar eventos futuros
        ShelterEventEntity futureEvent = eventList.get(0); 
        Long futureEventCode = futureEvent.getEventCode();
        assertThrows(IllegalStateException.class, () -> {
            eventService.deleteShelterEventByCode(futureEventCode);
        });
    }

    @Test
    void testDeletePastEventSuccess() {
        // Creamos manualmente un evento que YA PASÓ para poder borrarlo
        ShelterEventEntity pastEvent = factory.manufacturePojo(ShelterEventEntity.class);
        pastEvent.setShelter(sharedShelter);
        pastEvent.setEventDate(LocalDateTime.now().minusDays(10)); // Hace 10 días
        
        ShelterEventEntity persisted = entityManager.persistFlushFind(pastEvent);
        Long code = persisted.getEventCode();

        assertDoesNotThrow(() -> eventService.deleteShelterEventByCode(code));
        assertThrows(EntityNotFoundException.class, () -> {
            eventService.searchShelterEventByCode(code);
        });
    }
}