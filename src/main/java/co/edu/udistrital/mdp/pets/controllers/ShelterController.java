package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.ShelterDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @GetMapping
    public ResponseEntity<List<ShelterDTO>> getAll() {
        List<ShelterEntity> entities = shelterService.getShelters();
        List<ShelterDTO> dtos = entities.stream()
                .map(ShelterDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getById(@PathVariable Long id) {
        ShelterEntity entity = shelterService.searchShelter(id);
        return ResponseEntity.ok(new ShelterDTO(entity));
    }

    @PostMapping
    public ResponseEntity<ShelterDTO> create(@RequestBody ShelterEntity shelter) {
        ShelterEntity newShelter = shelterService.createShelter(shelter);
        return new ResponseEntity<>(new ShelterDTO(newShelter), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelterDTO> update(@PathVariable Long id, @RequestBody ShelterEntity shelter) {
        ShelterEntity updated = shelterService.updateShelter(id, shelter);
        return ResponseEntity.ok(new ShelterDTO(updated));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        shelterService.deleteShelter(id);
    }
}
