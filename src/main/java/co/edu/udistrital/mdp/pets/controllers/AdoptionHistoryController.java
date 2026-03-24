package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoption-histories")
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

    @PostMapping
    public ResponseEntity<AdoptionHistoryEntity> create(@RequestBody AdoptionHistoryEntity history) {
        return new ResponseEntity<>(adoptionHistoryService.createAdoptionHistory(history), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionHistoryEntity>> getAll() {
        return new ResponseEntity<>(adoptionHistoryService.getAdoptionHistories(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionHistoryEntity> getById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionHistoryService.getAdoptionHistory(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionHistoryEntity> update(@PathVariable Long id, @RequestBody AdoptionHistoryEntity history) {
        return new ResponseEntity<>(adoptionHistoryService.updateAdoptionHistory(id, history), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptionHistoryService.deleteAdoptionHistory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
