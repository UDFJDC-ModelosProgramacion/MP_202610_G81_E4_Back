package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.ReportService;
import jakarta.persistence.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ReportService.class)
public class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private List<ReportEntity> reportList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReportEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.flush();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);

            ReportEntity report = factory.manufacturePojo(ReportEntity.class);
            report.setShelter(shelter);
            report.setStartDate(LocalDate.now().minusDays(30));
            report.setEndDate(LocalDate.now());
            
            entityManager.persist(report);
            reportList.add(report);
        }
        entityManager.flush();
    }

    @Test
    void testCreateReport() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        ReportEntity newEntity = new ReportEntity();
        newEntity.setReportType("MENSUAL");
        newEntity.setStartDate(LocalDate.now().minusDays(10));
        newEntity.setEndDate(LocalDate.now());
        newEntity.setData("Contenido de prueba");
        newEntity.setShelter(shelter);

        ReportEntity result = reportService.createReport(newEntity);

        assertNotNull(result);
        ReportEntity entity = entityManager.find(ReportEntity.class, result.getId());
        assertEquals(newEntity.getReportType(), entity.getReportType());
    }

    @Test
    void testCreateReportInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            ReportEntity entity = new ReportEntity();
            entity.setReportType(""); 
            entity.setShelter(null);
            reportService.createReport(entity);
        });
    }

    @Test
    void testGetReport() {
        ReportEntity entity = reportList.get(0);
        ReportEntity resultEntity = reportService.getReport(entity.getId());
        
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
    }

    @Test
    void testUpdateReport() {
        ReportEntity entity = reportList.get(0);
        ReportEntity newDetails = new ReportEntity();
        newDetails.setReportType("ACTUALIZADO");

        ReportEntity result = reportService.updateReport(entity.getId(), newDetails);

        assertNotNull(result);
        assertEquals("ACTUALIZADO", result.getReportType());
    }

    @Test
    void testDeleteReport() {
        ReportEntity entity = reportList.get(0);
        reportService.deleteReport(entity.getId());
        
        ReportEntity deleted = entityManager.find(ReportEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testGetReportNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reportService.getReport(999L);
        });
    }
}