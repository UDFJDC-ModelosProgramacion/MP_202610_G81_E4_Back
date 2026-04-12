package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.DevolutionDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.services.DevolutionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devolutions")
public class DevolutionController {

    @Autowired
    private DevolutionService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody DevolutionDTO dto) {
        try {
            DevolutionEntity entity = toEntity(dto);
            DevolutionDTO response = new DevolutionDTO(service.create(entity));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<DevolutionDTO>> getAll() {
        List<DevolutionDTO> list = new ArrayList<>();
        for (DevolutionEntity e : service.findAll()) {
            list.add(new DevolutionDTO(e));
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new DevolutionDTO(service.findById(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody DevolutionDTO dto) {
        try {
            DevolutionEntity entity = toEntity(dto);
            return ResponseEntity.ok(new DevolutionDTO(service.update(id, entity)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    private DevolutionEntity toEntity(DevolutionDTO dto) {
        if (dto == null) return null;
        DevolutionEntity e = new DevolutionEntity();
        e.setReturnDate(dto.getReturnDate());
        e.setReason(dto.getReason());
        e.setDetailedDescription(dto.getDetailedDescription());
        e.setPetState(dto.getPetState());

        if (dto.getAdoptionId() != null) {
            AdoptionEntity adoption = new AdoptionEntity();
            adoption.setId(dto.getAdoptionId());
            e.setAdoption(adoption);
        }
        return e;
    }
}