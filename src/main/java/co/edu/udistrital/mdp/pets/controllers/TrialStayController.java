package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.services.TrialStayService;

import java.util.List;

@RestController
@RequestMapping("/trial-stays")
public class TrialStayController {

    @Autowired
    private TrialStayService trialStayService;

    @PostMapping
    public ResponseEntity<TrialStayEntity> create(@RequestBody TrialStayEntity entity) {
        TrialStayEntity created = trialStayService.createTrialStay(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<TrialStayEntity>> getAll() {
        return ResponseEntity.ok(trialStayService.searchAllTrialStays());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialStayEntity> getById(@PathVariable Long id) {
        return ResponseEntity.ok(trialStayService.searchTrialStay(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialStayEntity> update(
            @PathVariable Long id,
            @RequestBody TrialStayEntity entity) {

        TrialStayEntity updated = trialStayService.updateTrialStay(id, entity);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        trialStayService.deleteTrialStay(id);
        return ResponseEntity.noContent().build();
    }
}