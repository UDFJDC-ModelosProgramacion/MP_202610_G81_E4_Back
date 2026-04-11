package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.AdoptionTrackingDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoption-trackings")
public class AdoptionTrackingController {

    @Autowired
    private AdoptionTrackingService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdoptionTrackingDTO dto) {
        try {
            AdoptionTrackingEntity entity = toEntity(dto);
            AdoptionTrackingDTO response = new AdoptionTrackingDTO(service.create(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionTrackingDTO>> getAll() {
        List<AdoptionTrackingDTO> list = new ArrayList<>();
        for (AdoptionTrackingEntity e : service.findAll()) {
            list.add(new AdoptionTrackingDTO(e));
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new AdoptionTrackingDTO(service.findById(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AdoptionTrackingDTO dto) {
        try {
            AdoptionTrackingEntity entity = toEntity(dto);
            return ResponseEntity.ok(new AdoptionTrackingDTO(service.updateAdoptionTracking(id, entity)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteAdoptionTracking(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    private AdoptionTrackingEntity toEntity(AdoptionTrackingDTO dto) {
        if (dto == null) return null;
        AdoptionTrackingEntity e = new AdoptionTrackingEntity();
        e.setId(dto.getId());
        e.setFrequency(dto.getFrequency());
        e.setNotes(dto.getNotes());
        e.setNextReview(dto.getNextReview());

        if (dto.getAdoptionId() != null) {
            AdoptionEntity adoption = new AdoptionEntity();
            adoption.setId(dto.getAdoptionId());
            e.setAdoption(adoption);
        }
        return e;
    }
}