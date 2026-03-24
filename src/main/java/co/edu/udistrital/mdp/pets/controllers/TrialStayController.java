package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.services.TrialStayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trial-stays")
public class TrialStayController {

    @Autowired
    private TrialStayService trialStayService;

    @PostMapping
    public ResponseEntity<TrialStayEntity> createTrialStay(@RequestBody TrialStayEntity trialStay) {
        TrialStayEntity newStay = trialStayService.createTrialStay(trialStay);
        return new ResponseEntity<>(newStay, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TrialStayEntity>> getAllTrialStays() {
        List<TrialStayEntity> stays = trialStayService.searchAllTrialStays();
        return new ResponseEntity<>(stays, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialStayEntity> getTrialStayById(@PathVariable Long id) {
        TrialStayEntity stay = trialStayService.searchTrialStay(id);
        return new ResponseEntity<>(stay, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialStayEntity> update(@PathVariable Long id, @RequestBody TrialStayEntity trialStay) {
    return ResponseEntity.ok(trialStayService.updateTrialStay(id, trialStay));
}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrialStay(@PathVariable Long id) {
        trialStayService.deleteTrialStay(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}