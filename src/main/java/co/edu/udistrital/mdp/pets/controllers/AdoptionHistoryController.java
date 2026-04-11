package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.AdoptionHistoryDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionHistoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoption-histories")
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdoptionHistoryDTO dto) {
        try {
            AdoptionHistoryEntity entity = toEntity(dto);
            AdoptionHistoryDTO response = new AdoptionHistoryDTO(service.createAdoptionHistory(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionHistoryDTO>> getAll() {
        List<AdoptionHistoryDTO> list = new ArrayList<>();
        for (AdoptionHistoryEntity e : service.getAdoptionHistories()) {
            list.add(new AdoptionHistoryDTO(e));
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            AdoptionHistoryEntity entity = service.getAdoptionHistory(id);
            return ResponseEntity.ok(new AdoptionHistoryDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody AdoptionHistoryDTO dto) {
        try {
            AdoptionHistoryEntity entity = toEntity(dto);
            AdoptionHistoryEntity updated = service.updateAdoptionHistory(id, entity);
            return ResponseEntity.ok(new AdoptionHistoryDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteAdoptionHistory(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    private AdoptionHistoryEntity toEntity(AdoptionHistoryDTO dto) {
        if (dto == null) return null;

        AdoptionHistoryEntity e = new AdoptionHistoryEntity();
        e.setDate(dto.getDate());
        e.setReason(dto.getReason());
        e.setDetail(dto.getDetail());

        if (dto.getAdoptionId() != null) {
            AdoptionEntity adoption = new AdoptionEntity();
            adoption.setId(dto.getAdoptionId());
            e.setAdoption(adoption);
        }

        return e;
    }
}
