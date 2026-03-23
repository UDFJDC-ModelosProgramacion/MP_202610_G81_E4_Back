package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.repositories.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReporteRepository repository;

    // Crear
    public ReportEntity createReport(ReportEntity report) {
        if (report.getShelter() == null)
            throw new IllegalArgumentException("El reporte debe tener un refugio asociado");
        if (report.getReportType() == null || report.getReportType().isEmpty())
            throw new IllegalArgumentException("El tipo de reporte no puede ser nulo o vacío");
        if (report.getStartDate() != null && report.getEndDate() != null
                && report.getStartDate().after(report.getEndDate()))
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        return repository.save(report);
    }

    // Obtener todos
    public List<ReportEntity> getReports() {
        return repository.findAll();
    }

    // Obtener uno por ID
    public ReportEntity getReport(Long id) {
        Optional<ReportEntity> report = repository.findById(id.intValue());
        if (report.isEmpty())
            throw new IllegalArgumentException("El reporte con id " + id + " no existe");
        return report.get();
    }

    // Actualizar
    public ReportEntity updateReport(Long id, ReportEntity report) {
        Optional<ReportEntity> existing = repository.findById(id.intValue());
        if (existing.isEmpty())
            throw new IllegalArgumentException("El reporte con id " + id + " no existe");
        if (report.getReportType() == null || report.getReportType().isEmpty())
            throw new IllegalArgumentException("El tipo de reporte no puede ser nulo o vacío");
        if (report.getStartDate() != null && report.getEndDate() != null
                && report.getStartDate().after(report.getEndDate()))
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        ReportEntity toUpdate = existing.get();
        toUpdate.setReportType(report.getReportType());
        toUpdate.setStartDate(report.getStartDate());
        toUpdate.setEndDate(report.getEndDate());
        toUpdate.setData(report.getData());
        toUpdate.setGenerationDate(report.getGenerationDate());
        toUpdate.setShelter(report.getShelter());
        return repository.save(toUpdate);
    }

    // Eliminar
    public void deleteReport(Long id) {
        Optional<ReportEntity> report = repository.findById(id.intValue());
        if (report.isEmpty())
            throw new IllegalArgumentException("El reporte con id " + id + " no existe");
        repository.deleteById(id.intValue());
    }
}