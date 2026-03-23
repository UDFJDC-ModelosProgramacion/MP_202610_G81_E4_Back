package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.repositories.DevolucionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DevolutionServiceTest {

    @Mock
    private DevolucionRepository repository;

    @InjectMocks
    private DevolutionService service;

    private DevolutionEntity devolution;
    private AdoptionEntity adoption;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adoption = new AdoptionEntity();
        devolution = new DevolutionEntity();
        devolution.setAdoption(adoption);
        devolution.setReason("No puede cuidarlo");
        devolution.setDetailedDescription("El adoptante se mudó");
        devolution.setPetState("Saludable");
        devolution.setReturnDate(new Date());
    }

    // CREATE - datos correctos
    @Test
    void testCreateDevolutionOk() {
        when(repository.findByAdoption(adoption)).thenReturn(List.of());
        when(repository.save(devolution)).thenReturn(devolution);
        DevolutionEntity result = service.createDevolution(devolution);
        assertNotNull(result);
        assertEquals("No puede cuidarlo", result.getReason());
    }

    // CREATE - sin adopcion
    @Test
    void testCreateDevolutionNoAdoption() {
        devolution.setAdoption(null);
        assertThrows(IllegalArgumentException.class, () -> service.createDevolution(devolution));
    }

    // CREATE - motivo vacio
    @Test
    void testCreateDevolutionEmptyReason() {
        devolution.setReason("");
        assertThrows(IllegalArgumentException.class, () -> service.createDevolution(devolution));
    }

    // CREATE - ya existe devolución para esa adopción
    @Test
    void testCreateDevolutionDuplicate() {
        when(repository.findByAdoption(adoption)).thenReturn(List.of(devolution));
        assertThrows(IllegalArgumentException.class, () -> service.createDevolution(devolution));
    }

    // GET - existe
    @Test
    void testGetDevolutionOk() {
        when(repository.findById(1)).thenReturn(Optional.of(devolution));
        DevolutionEntity result = service.getDevolution(1L);
        assertNotNull(result);
    }

    // GET - no existe
    @Test
    void testGetDevolutionNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getDevolution(1L));
    }

    // UPDATE - motivo vacio
    @Test
    void testUpdateDevolutionEmptyReason() {
        when(repository.findById(1)).thenReturn(Optional.of(devolution));
        devolution.setReason("");
        assertThrows(IllegalArgumentException.class, () -> service.updateDevolution(1L, devolution));
    }

    // UPDATE - no existe
    @Test
    void testUpdateDevolutionNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateDevolution(1L, devolution));
    }

    // DELETE - existe
    @Test
    void testDeleteDevolutionOk() {
        when(repository.findById(1)).thenReturn(Optional.of(devolution));
        assertDoesNotThrow(() -> service.deleteDevolution(1L));
    }

    // DELETE - no existe
    @Test
    void testDeleteDevolutionNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteDevolution(1L));
    }
}