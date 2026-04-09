package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public List<PetEntity> getAll() {
        return petService.getPets();
    }

    @GetMapping("/{id}")
    public PetEntity getById(@PathVariable Long id) throws EntityNotFoundException {
        return petService.getPet(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetEntity create(@RequestBody PetEntity pet) throws IllegalOperationException {
        return petService.createPet(pet);
    }

    @PutMapping("/{id}")
    public PetEntity update(@PathVariable Long id, @RequestBody PetEntity pet) 
            throws EntityNotFoundException, IllegalOperationException {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(id);
    }
}