package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.repositories.SeguimientoAdopcionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdoptionTrackingServiceTest {

    @Mock
    private SeguimientoAdopcionRepository repository;

    @InjectMocks
    private AdoptionTrackingService service;

    private AdoptionTrackingEntity tracking;
    private AdoptionEntity adoption;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adoption = new AdoptionEntity();
        tracking = new AdoptionTrackingEntity();
        tracking.setAdoption(adoption);
        tracking.setFrequency("Mensual");
        tracking.setNotes("Todo bien");
        tracking.setNextReview(new Date());
    }

    // CREATE - datos correctos
    @Test
    void testCreateAdoptionTrackingOk() {
        when(repository.save(tracking)).thenReturn(tracking);
        AdoptionTrackingEntity result = service.createAdoptionTracking(tracking);
        assertNotNull(result);
        assertEquals("Mensual", result.getFrequency());
    }

    // CREATE - sin adopcion
    @Test
    void testCreateAdoptionTrackingNoAdoption() {
        tracking.setAdoption(null);
        assertThrows(IllegalArgumentException.class, () -> service.createAdoptionTracking(tracking));
    }

    // CREATE - frecuencia vacia
    @Test
    void testCreateAdoptionTrackingEmptyFrequency() {
        tracking.setFrequency("");
        assertThrows(IllegalArgumentException.class, () -> service.createAdoptionTracking(tracking));
    }

    // GET - existe
    @Test
    void testGetAdoptionTrackingOk() {
        when(repository.findById(1)).thenReturn(Optional.of(tracking));
        AdoptionTrackingEntity result = service.getAdoptionTracking(1L);
        assertNotNull(result);
    }

    // GET - no existe
    @Test
    void testGetAdoptionTrackingNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getAdoptionTracking(1L));
    }

    // UPDATE - fecha nula
    @Test
    void testUpdateAdoptionTrackingNullDate() {
        when(repository.findById(1)).thenReturn(Optional.of(tracking));
        tracking.setNextReview(null);
        assertThrows(IllegalArgumentException.class, () -> service.updateAdoptionTracking(1L, tracking));
    }

    // UPDATE - no existe
    @Test
    void testUpdateAdoptionTrackingNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateAdoptionTracking(1L, tracking));
    }

    // DELETE - existe
    @Test
    void testDeleteAdoptionTrackingOk() {
        when(repository.findById(1)).thenReturn(Optional.of(tracking));
        assertDoesNotThrow(() -> service.deleteAdoptionTracking(1L));
    }

    // DELETE - no existe
    @Test
    void testDeleteAdoptionTrackingNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteAdoptionTracking(1L));
    }
}