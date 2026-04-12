package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.DevolutionDTO;
import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.services.DevolutionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devolutions")
public class DevolutionController {
    private static final String ERR_MSG = "message";

    @Autowired
    private DevolutionService devolutionService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody DevolutionEntity devolution) {
        try {
            DevolutionEntity created = devolutionService.createDevolution(devolution);
            return new ResponseEntity<>(new DevolutionDTO(created), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<DevolutionDTO>> getAll() {
        List<DevolutionDTO> dtos = devolutionService.searchDevolutions().stream()
                .map(DevolutionDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            DevolutionEntity entity = devolutionService.searchDevolution(id);
            return ResponseEntity.ok(new DevolutionDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            devolutionService.deleteDevolution(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }
}