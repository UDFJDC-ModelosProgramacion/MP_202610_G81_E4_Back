package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoption-tracking")
public class AdoptionTrackingController {

    @Autowired
    private AdoptionTrackingService service;

    @PostMapping
    public ResponseEntity<AdoptionTrackingEntity> createAdoptionTracking(
            @RequestBody AdoptionTrackingEntity tracking) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.createAdoptionTracking(tracking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionTrackingEntity>> getAdoptionTrackings() {
        return ResponseEntity.ok(service.getAdoptionTrackings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionTrackingEntity> getAdoptionTracking(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getAdoptionTracking(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionTrackingEntity> updateAdoptionTracking(
            @PathVariable Long id, @RequestBody AdoptionTrackingEntity tracking) {
        try {
            return ResponseEntity.ok(service.updateAdoptionTracking(id, tracking));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoptionTracking(@PathVariable Long id) {
        try {
            service.deleteAdoptionTracking(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}