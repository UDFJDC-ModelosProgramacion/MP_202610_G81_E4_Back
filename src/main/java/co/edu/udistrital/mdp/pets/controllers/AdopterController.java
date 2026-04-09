package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.services.AdopterService;

import java.util.List;

@RestController
@RequestMapping("/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @PostMapping
    public ResponseEntity<AdopterEntity> create(@RequestBody AdopterEntity adopter) {
    AdopterEntity created = adopterService.createAdopter(adopter);
    return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<AdopterEntity>> getAll() {
        return ResponseEntity.ok(adopterService.searchAdopters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopterEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(adopterService.searchAdopter(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdopterEntity> update(@PathVariable Long id, @RequestBody AdopterEntity adopter) {
        return ResponseEntity.ok(adopterService.updateAdopter(id, adopter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adopterService.deleteAdopter(id);
        return ResponseEntity.noContent().build();
    }
}
