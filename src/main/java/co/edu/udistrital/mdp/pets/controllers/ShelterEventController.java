package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shelter-events")
public class ShelterEventController {

    @Autowired
    private ShelterEventService shelterEventService;

    @PostMapping
    public ResponseEntity<ShelterEventEntity> create(@RequestBody ShelterEventEntity event) {
        return new ResponseEntity<>(shelterEventService.createShelterEvent(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShelterEventEntity>> getAll() {
        return ResponseEntity.ok(shelterEventService.searchAllShelterEvents());
    }

    @GetMapping("/code/{eventCode}")
    public ResponseEntity<ShelterEventEntity> getByCode(@PathVariable Long eventCode) {
        return ResponseEntity.ok(shelterEventService.searchShelterEventByCode(eventCode));
    }

    @DeleteMapping("/code/{eventCode}")
    public ResponseEntity<Void> delete(@PathVariable Long eventCode) {
        shelterEventService.deleteShelterEventByCode(eventCode);
        return ResponseEntity.noContent().build();
    }
}
