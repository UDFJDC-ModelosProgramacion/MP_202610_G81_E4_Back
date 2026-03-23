package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.services.DevolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devolution")
public class DevolutionController {

    @Autowired
    private DevolutionService service;

    @PostMapping
    public ResponseEntity<DevolutionEntity> createDevolution(
            @RequestBody DevolutionEntity devolution) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(service.createDevolution(devolution));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<DevolutionEntity>> getDevolutions() {
        return ResponseEntity.ok(service.getDevolutions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevolutionEntity> getDevolution(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.getDevolution(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DevolutionEntity> updateDevolution(
            @PathVariable Long id, @RequestBody DevolutionEntity devolution) {
        try {
            return ResponseEntity.ok(service.updateDevolution(id, devolution));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevolution(@PathVariable Long id) {
        try {
            service.deleteDevolution(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}