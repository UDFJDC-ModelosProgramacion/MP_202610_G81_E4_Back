package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.repositories.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;

    public ReportEntity createReport(ReportEntity report) {
        log.info("Creating report");
        
        if (report == null) 
            throw new IllegalArgumentException("Report cannot be null");
        if (report.getShelter() == null || report.getShelter().getId() == null)
            throw new IllegalArgumentException("Report must have an associated shelter");
        if (report.getReportType() == null || report.getReportType().isEmpty())
            throw new IllegalArgumentException("Report type cannot be null or empty");
            
        return repository.save(report);
    }

    public List<ReportEntity> getReports() {
        log.info("Searching all reports");
        return repository.findAll();
    }

    public ReportEntity getReport(Long id) {
        log.info("Searching report with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report with id " + id + " does not exist"));
    }

    public ReportEntity updateReport(Long id, ReportEntity report) {
        log.info("Updating report with id: {}", id);
        
        ReportEntity existing = getReport(id); 
        
        if (report != null) {
            if (report.getReportType() != null) existing.setReportType(report.getReportType());
            if (report.getStartDate() != null) existing.setStartDate(report.getStartDate());
            if (report.getEndDate() != null) existing.setEndDate(report.getEndDate());
            if (report.getData() != null) existing.setData(report.getData());
            
            if (report.getShelter() != null && report.getShelter().getId() != null) {
                existing.setShelter(report.getShelter());
            }
        }
        
        return repository.save(existing);
    }

    public void deleteReport(Long id) {
        log.info("Deleting report with id: {}", id);
        ReportEntity report = getReport(id);
        repository.delete(report);
    }
}