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
    public ResponseEntity<ShelterEventEntity> createEvent(@RequestBody ShelterEventEntity event) {
        ShelterEventEntity newEvent = shelterEventService.createShelterEvent(event);
        return new ResponseEntity<>(newEvent, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<ShelterEventEntity>> getAllEvents() {
        List<ShelterEventEntity> events = shelterEventService.searchAllShelterEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }
    @GetMapping("/code/{eventCode}")
    public ResponseEntity<ShelterEventEntity> getEventByCode(@PathVariable Integer eventCode) {
        ShelterEventEntity event = shelterEventService.searchShelterEventByCode(eventCode);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
    @PutMapping("/code/{eventCode}")
    public ResponseEntity<ShelterEventEntity> updateEvent(@PathVariable Integer eventCode, @RequestBody ShelterEventEntity event) {
        ShelterEventEntity updatedEvent = shelterEventService.updateShelterEventByCode(eventCode, event);
        return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
    }
    @DeleteMapping("/code/{eventCode}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventCode) {
        shelterEventService.deleteShelterEventByCode(eventCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
