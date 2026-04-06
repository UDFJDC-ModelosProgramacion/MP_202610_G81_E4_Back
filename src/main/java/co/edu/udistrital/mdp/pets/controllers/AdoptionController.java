package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<AdoptionEntity> create(@RequestBody AdoptionEntity adoption) {
        AdoptionEntity created = adoptionService.createAdoption(adoption);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionEntity>> getAll() {
        return ResponseEntity.ok(adoptionService.searchAdoptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adoptionService.searchAdoption(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionEntity> update(
            @PathVariable Long id,
            @RequestBody AdoptionEntity adoption) {
        return ResponseEntity.ok(adoptionService.updateAdoption(id, adoption));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}