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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoption-histories")
public class AdoptionHistoryController {

    // 1. Constante para evitar duplicación de literales (SonarQube)
    private static final String ERR_MSG = "message";

    @Autowired
    private AdoptionHistoryService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody AdoptionHistoryDTO dto) {
        try {
            AdoptionHistoryEntity entity = toEntity(dto);
            AdoptionHistoryDTO response = new AdoptionHistoryDTO(service.createAdoptionHistory(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<AdoptionHistoryDTO>> getAll() {
        // 2. Uso de .toList() en lugar de bucles manuales o collect
        List<AdoptionHistoryDTO> list = service.getAdoptionHistories().stream()
                .map(AdoptionHistoryDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            AdoptionHistoryEntity entity = service.getAdoptionHistory(id);
            return ResponseEntity.ok(new AdoptionHistoryDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody AdoptionHistoryDTO dto) {
        try {
            AdoptionHistoryEntity entity = toEntity(dto);
            AdoptionHistoryEntity updated = service.updateAdoptionHistory(id, entity);
            return ResponseEntity.ok(new AdoptionHistoryDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteAdoptionHistory(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
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
