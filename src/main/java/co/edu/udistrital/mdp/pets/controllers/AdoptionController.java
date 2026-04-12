package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionService;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<AdoptionDTO> create(@RequestBody AdoptionEntity adoption) 
            throws IllegalOperationException, EntityNotFoundException {
        AdoptionEntity created = adoptionService.createAdoption(adoption);
        return new ResponseEntity<>(new AdoptionDTO(created), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AdoptionDTO>> getAll() {
        List<AdoptionEntity> entities = adoptionService.searchAdoptions();
        return ResponseEntity.ok(entities.stream().map(AdoptionDTO::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdoptionDTO> getById(@PathVariable Long id) 
            throws EntityNotFoundException {
        AdoptionEntity entity = adoptionService.searchAdoption(id);
        return ResponseEntity.ok(new AdoptionDTO(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdoptionDTO> update(
            @PathVariable Long id,
            @RequestBody AdoptionEntity adoption) 
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity updated = adoptionService.updateAdoption(id, adoption);
        return ResponseEntity.ok(new AdoptionDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(id);
        return ResponseEntity.noContent().build();
    }
}