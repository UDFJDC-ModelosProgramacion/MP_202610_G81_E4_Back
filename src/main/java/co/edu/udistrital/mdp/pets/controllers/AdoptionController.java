package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<AdoptionEntity> createAdoption(@RequestBody AdoptionEntity adoption) {
        AdoptionEntity newAdoption = adoptionService.createAdoption(adoption);
        return new ResponseEntity<>(newAdoption, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionEntity>> getAllAdoptions() {
        List<AdoptionEntity> adoptions = adoptionService.searchAdoptions();
        return new ResponseEntity<>(adoptions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionEntity> getAdoptionById(@PathVariable Long id) {
        AdoptionEntity adoption = adoptionService.searchAdoption(id);
        return new ResponseEntity<>(adoption, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionEntity> updateAdoption(@PathVariable Long id, @RequestBody AdoptionEntity adoption) {
        AdoptionEntity updated = adoptionService.updateAdoption(id, adoption);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdoption(@PathVariable Long id) {
        adoptionService.deleteAdoption(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}