package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.ReportDTO;
import co.edu.udistrital.mdp.pets.dto.ReportDetailDTO;
import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.ReportService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private static final String ERR_MSG = "message";

    @Autowired
    private ReportService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ReportDTO dto) {
        try {
            ReportEntity entity = toEntity(dto);
            ReportDTO response = new ReportDTO(service.createReport(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAll() {
        List<ReportDTO> list = service.getReports().stream()
                .map(ReportDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ReportDetailDTO(service.getReport(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ReportDTO dto) {
        try {
            ReportEntity entity = toEntity(dto);
            return ResponseEntity.ok(new ReportDTO(service.updateReport(id, entity)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    private ReportEntity toEntity(ReportDTO dto) {
        if (dto == null) return null;

        ReportEntity e = new ReportEntity();
        e.setId(dto.getId());
        e.setReportType(dto.getReportType());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setData(dto.getData());
        e.setGenerationDate(dto.getGenerationDate() != null ? 
                            dto.getGenerationDate() : java.time.LocalDate.now());

        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            e.setShelter(shelter);
        }

        return e;
    }
}