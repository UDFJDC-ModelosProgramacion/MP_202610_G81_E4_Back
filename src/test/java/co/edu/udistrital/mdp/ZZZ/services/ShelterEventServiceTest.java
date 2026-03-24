package co.edu.udistrital.mdp.ZZZ.services;

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
            // Persistir Shelter primero (es obligatorio para el evento)
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);

            // Persistir Evento asociado al Shelter
            ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
            event.setShelter(shelter);
            event.setEventCode(1000 + i); 
            event.setEventDate(LocalDateTime.now().plusDays(5)); // Fecha futura para evitar validaciones
            entityManager.persist(event);
            eventList.add(event);
        }
        entityManager.flush();
    }

    @Test
    void testCreateShelterEvent() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        ShelterEventEntity newEvent = factory.manufacturePojo(ShelterEventEntity.class);
        newEvent.setShelter(shelter);
        newEvent.setEventDate(LocalDateTime.now().plusDays(2));
        newEvent.setEventCode(null); // El service debe generar uno aleatorio

        ShelterEventEntity result = shelterEventService.createShelterEvent(newEvent);

        assertNotNull(result);
        assertNotNull(result.getEventCode());
        assertEquals(shelter.getId(), result.getShelter().getId());
    }

    @Test
    void testSearchEventByCode() {
        ShelterEventEntity target = eventList.get(0);
        ShelterEventEntity result = shelterEventService.searchShelterEventByCode(target.getEventCode());

        assertNotNull(result);
        assertEquals(target.getEventCode(), result.getEventCode());
    }

    @Test
    void testSearchEventNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterEventService.searchShelterEventByCode(9999);
        });
    }

    @Test
    void testUpdateEventByCode() {
        ShelterEventEntity target = eventList.get(0);
        
        ShelterEventEntity updateData = factory.manufacturePojo(ShelterEventEntity.class);
        updateData.setEventDate(LocalDateTime.now().plusDays(10));
        updateData.setShelter(target.getShelter()); // El validador pide que no sea nulo

        ShelterEventEntity result = shelterEventService.updateShelterEventByCode(target.getEventCode(), updateData);

        assertNotNull(result);
        assertEquals(updateData.getEventDate(), result.getEventDate());
        assertEquals(updateData.getTitle(), result.getTitle());
    }

    @Test
    void testDeleteEventFutureThrowsException() {
        // La regla de negocio prohibe borrar eventos futuros
        ShelterEventEntity futureEvent = eventList.get(0); 
        
        assertThrows(IllegalStateException.class, () -> {
            shelterEventService.deleteShelterEventByCode(futureEvent.getEventCode());
        });
    }

    @Test
    void testDeleteEventPastSuccess() {
        // Crear un evento del pasado manualmente
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        ShelterEventEntity pastEvent = factory.manufacturePojo(ShelterEventEntity.class);
        pastEvent.setShelter(shelter);
        pastEvent.setEventDate(LocalDateTime.now().minusDays(5)); // Pasado
        pastEvent.setEventCode(8888);
        entityManager.persist(pastEvent);
        entityManager.flush();

        // No debe lanzar excepción al borrar algo pasado
        assertDoesNotThrow(() -> shelterEventService.deleteShelterEventByCode(8888));
        
        // Verificar que ya no existe
        assertThrows(EntityNotFoundException.class, () -> {
            shelterEventService.searchShelterEventByCode(8888);
        });
    }

    @Test
    void testCreateEventPastDateThrowsException() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        ShelterEventEntity event = factory.manufacturePojo(ShelterEventEntity.class);
        event.setShelter(shelter);
        event.setEventDate(LocalDateTime.now().minusDays(1)); // Intento de crear en el pasado

        assertThrows(IllegalArgumentException.class, () -> {
            shelterEventService.createShelterEvent(event);
        });
    }
}