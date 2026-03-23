package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.repositories.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    @Mock
    private ReporteRepository repository;

    @InjectMocks
    private ReportService service;

    private ReportEntity report;
    private ShelterEntity shelter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        shelter = new ShelterEntity();
        report = new ReportEntity();
        report.setShelter(shelter);
        report.setReportType("Mensual");
        report.setStartDate(new Date(2026 - 1900, 0, 1));
        report.setEndDate(new Date(2026 - 1900, 11, 31));
        report.setData("Datos del reporte");
        report.setGenerationDate(new Date());
    }

    // CREATE - datos correctos
    @Test
    void testCreateReportOk() {
        when(repository.save(report)).thenReturn(report);
        ReportEntity result = service.createReport(report);
        assertNotNull(result);
        assertEquals("Mensual", result.getReportType());
    }

    // CREATE - sin refugio
    @Test
    void testCreateReportNoShelter() {
        report.setShelter(null);
        assertThrows(IllegalArgumentException.class, () -> service.createReport(report));
    }

    // CREATE - tipo vacio
    @Test
    void testCreateReportEmptyType() {
        report.setReportType("");
        assertThrows(IllegalArgumentException.class, () -> service.createReport(report));
    }

    // CREATE - fecha inicio posterior a fecha fin
    @Test
    void testCreateReportInvalidDates() {
        report.setStartDate(new Date(2026 - 1900, 11, 31));
        report.setEndDate(new Date(2026 - 1900, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> service.createReport(report));
    }

    // GET - existe
    @Test
    void testGetReportOk() {
        when(repository.findById(1)).thenReturn(Optional.of(report));
        ReportEntity result = service.getReport(1L);
        assertNotNull(result);
    }

    // GET - no existe
    @Test
    void testGetReportNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.getReport(1L));
    }

    // UPDATE - tipo vacio
    @Test
    void testUpdateReportEmptyType() {
        when(repository.findById(1)).thenReturn(Optional.of(report));
        report.setReportType("");
        assertThrows(IllegalArgumentException.class, () -> service.updateReport(1L, report));
    }

    // UPDATE - fechas invalidas
    @Test
    void testUpdateReportInvalidDates() {
        when(repository.findById(1)).thenReturn(Optional.of(report));
        report.setStartDate(new Date(2026 - 1900, 11, 31));
        report.setEndDate(new Date(2026 - 1900, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> service.updateReport(1L, report));
    }

    // UPDATE - no existe
    @Test
    void testUpdateReportNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.updateReport(1L, report));
    }

    // DELETE - existe
    @Test
    void testDeleteReportOk() {
        when(repository.findById(1)).thenReturn(Optional.of(report));
        assertDoesNotThrow(() -> service.deleteReport(1L));
    }

    // DELETE - no existe
    @Test
    void testDeleteReportNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteReport(1L));
    }
}