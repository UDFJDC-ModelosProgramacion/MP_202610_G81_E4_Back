package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoption-history")
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService service;

    @PostMapping
    public ResponseEntity<AdoptionHistoryEntity> createAdoptionHistory(
            @RequestBody AdoptionHistoryEntity history) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.createAdoptionHistory(history));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionHistoryEntity>> getAdoptionHistories() {
        return ResponseEntity.ok(service.getAdoptionHistories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionHistoryEntity> getAdoptionHistory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getAdoptionHistory(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionHistoryEntity> updateAdoptionHistory(
            @PathVariable Long id, @RequestBody AdoptionHistoryEntity history) {
        try {
            return ResponseEntity.ok(service.updateAdoptionHistory(id, history));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoptionHistory(@PathVariable Long id) {
        try {
            service.deleteAdoptionHistory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
