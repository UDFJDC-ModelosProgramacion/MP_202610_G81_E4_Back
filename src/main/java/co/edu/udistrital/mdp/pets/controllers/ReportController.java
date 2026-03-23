package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import co.edu.udistrital.mdp.pets.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService service;

    @PostMapping
    public ResponseEntity<ReportEntity> createReport(
            @RequestBody ReportEntity report) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.createReport(report));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ReportEntity>> getReports() {
        return ResponseEntity.ok(service.getReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportEntity> getReport(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getReport(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportEntity> updateReport(
            @PathVariable Long id, @RequestBody ReportEntity report) {
        try {
            return ResponseEntity.ok(service.updateReport(id, report));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        try {
            service.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}