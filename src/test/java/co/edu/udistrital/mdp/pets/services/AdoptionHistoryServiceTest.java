package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.repositories.HistorialAdopcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdoptionHistoryServiceTest {

    @Mock
    private HistorialAdopcionRepository repository;

    @InjectMocks
    private AdoptionHistoryService service;

    private AdoptionHistoryEntity history;
    private AdoptionEntity adoption;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adoption = new AdoptionEntity();
        history = new AdoptionHistoryEntity();
        history.setAdoption(adoption);
        history.setReason("Adopción exitosa");
        history.setDetail("El animal se adaptó bien");
        history.setDate(new Date());
    }

    // CREATE - datos correctos
    @Test
    void testCreateAdoptionHistoryOk() {
        when(repository.save(history)).thenReturn(history);
        AdoptionHistoryEntity result = service.createAdoptionHistory(history);
        assertNotNull(result);
        assertEquals("Adopción exitosa", result.getReason());
    }

    // CREATE - sin adopcion
    @Test
    void testCreateAdoptionHistoryNoAdoption() {
        history.setAdoption(null);
        assertThrows(IllegalArgumentException.class, () -> service.createAdoptionHistory(history));
    }

    // CREATE - razon vacia
    @Test
    void testCreateAdoptionHistoryEmptyReason() {
        history.setReason("");
        assertThrows(IllegalArgumentException.class, () -> service.createAdoptionHistory(history));
    }

    // GET - existe
    @Test
    void testGetAdoptionHistoryOk() {
        when(repository.findById(1)).thenReturn(Optional.of(history));
        AdoptionHistoryEntity result = service.getAdoptionHistory(1L);
        assertNotNull(result);
    }

    // GET - no existe
    @Test
    void testGetAdoptionHistoryNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getAdoptionHistory(1L));
    }

    // UPDATE - razon vacia
    @Test
    void testUpdateAdoptionHistoryEmptyReason() {
        when(repository.findById(1)).thenReturn(Optional.of(history));
        history.setReason("");
        assertThrows(IllegalArgumentException.class, () -> service.updateAdoptionHistory(1L, history));
    }

    // UPDATE - no existe
    @Test
    void testUpdateAdoptionHistoryNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateAdoptionHistory(1L, history));
    }

    // DELETE - existe
    @Test
    void testDeleteAdoptionHistoryOk() {
        when(repository.findById(1)).thenReturn(Optional.of(history));
        assertDoesNotThrow(() -> service.deleteAdoptionHistory(1L));
    }

    // DELETE - no existe
    @Test
    void testDeleteAdoptionHistoryNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteAdoptionHistory(1L));
    }
}