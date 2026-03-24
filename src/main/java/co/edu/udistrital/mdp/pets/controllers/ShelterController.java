package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @PostMapping
    public ResponseEntity<ShelterEntity> createShelter(@RequestBody ShelterEntity shelter) {
        return new ResponseEntity<>(shelterService.createShelter(shelter), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelterEntity> getShelterById(@PathVariable Long id) {
        return new ResponseEntity<>(shelterService.searchShelter(id), HttpStatus.OK);
    }

   @PutMapping("/{id}")
    public ResponseEntity<ShelterEntity> updateShelter(@PathVariable Long id, @RequestBody ShelterEntity shelter) {
    return new ResponseEntity<>(shelterService.updateShelter(id, shelter), HttpStatus.OK);
    }

   @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShelter(@PathVariable Long id) {
    shelterService.deleteShelter(id);
}
}
