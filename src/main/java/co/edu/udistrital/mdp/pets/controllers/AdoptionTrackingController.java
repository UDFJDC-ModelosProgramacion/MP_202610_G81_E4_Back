package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adoption-trackings")
public class AdoptionTrackingController {

    @Autowired
    private AdoptionTrackingService adoptionTrackingService;

    @PostMapping
    public ResponseEntity<AdoptionTrackingEntity> create(@RequestBody AdoptionTrackingEntity tracking) {
        return new ResponseEntity<>(adoptionTrackingService.createAdoptionTracking(tracking), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionTrackingEntity>> getAll() {
        return new ResponseEntity<>(adoptionTrackingService.getAdoptionTrackings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionTrackingEntity> getById(@PathVariable Long id) {
        return new ResponseEntity<>(adoptionTrackingService.getAdoptionTracking(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionTrackingEntity> update(@PathVariable Long id, @RequestBody AdoptionTrackingEntity tracking) {
        return new ResponseEntity<>(adoptionTrackingService.updateAdoptionTracking(id, tracking), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adoptionTrackingService.deleteAdoptionTracking(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}