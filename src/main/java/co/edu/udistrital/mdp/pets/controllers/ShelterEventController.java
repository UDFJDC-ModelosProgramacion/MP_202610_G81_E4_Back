package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ShelterEventDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.services.ShelterEventService;

@RestController
@RequestMapping("/shelter-events")
public class ShelterEventController {

    @Autowired
    private ShelterEventService shelterEventService;

    @PostMapping
    public ResponseEntity<ShelterEventDTO> create(@RequestBody ShelterEventEntity event) {
        ShelterEventEntity created = shelterEventService.createShelterEvent(event);
        return new ResponseEntity<>(new ShelterEventDTO(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ShelterEventDTO>> getAll() {
        List<ShelterEventEntity> entities = shelterEventService.searchAllShelterEvents();
        List<ShelterEventDTO> dtos = entities.stream()
                .map(ShelterEventDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/code/{eventCode}")
    public ResponseEntity<ShelterEventDTO> getByCode(@PathVariable Long eventCode) {
        ShelterEventEntity entity = shelterEventService.searchShelterEventByCode(eventCode);
        return ResponseEntity.ok(new ShelterEventDTO(entity));
    }

    @PutMapping("/code/{eventCode}")
    public ResponseEntity<ShelterEventDTO> updateByCode(
            @PathVariable Long eventCode, 
            @RequestBody ShelterEventEntity event) {
        ShelterEventEntity updated = shelterEventService.updateShelterEventByCode(eventCode, event);
        return ResponseEntity.ok(new ShelterEventDTO(updated));
    }

    @DeleteMapping("/code/{eventCode}")
    public ResponseEntity<Void> deleteByCode(@PathVariable Long eventCode) {
        shelterEventService.deleteShelterEventByCode(eventCode);
        return ResponseEntity.noContent().build();
    }
}
