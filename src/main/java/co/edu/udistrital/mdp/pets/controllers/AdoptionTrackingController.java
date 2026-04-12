package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.AdoptionTrackingDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity; // Importación necesaria
import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoption-trackings")
public class AdoptionTrackingController {
    
    // Constante para evitar duplicación del literal "message" según Sonar
    private static final String ERR_MSG = "message";

    @Autowired
    private AdoptionTrackingService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody AdoptionTrackingDTO dto) {
        try {
            AdoptionTrackingEntity entity = toEntity(dto);
            // Asegúrate de que el método en el servicio sea el correcto (createAdoptionTracking)
            AdoptionTrackingDTO response = new AdoptionTrackingDTO(service.createAdoptionTracking(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionTrackingDTO>> getAll() {
        // Implementación real usando streams y .toList() para evitar mutabilidad
        List<AdoptionTrackingDTO> list = service.getAdoptionTrackings().stream()
                .map(AdoptionTrackingDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            AdoptionTrackingEntity entity = service.getAdoptionTracking(id);
            return ResponseEntity.ok(new AdoptionTrackingDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteAdoptionTracking(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
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