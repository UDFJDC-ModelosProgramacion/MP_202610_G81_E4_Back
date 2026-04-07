package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.services.PetService;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/pets")

public class PetController {
    @Autowired
    private PetService petService;
    @PostMapping
    public PetDTO create(@RequestBody PetDTO dto) throws IllegalOperationException {
        PetEntity entity = toEntity(dto);
        PetEntity newPet = petService.createPet(entity);
        return toDTO(newPet);
    }

    @GetMapping
    public List<PetDTO> findAll() {
        List<PetEntity> entities = petService.getPets();
        List<PetDTO> dtos = new ArrayList<>();

        for (PetEntity entity : entities) {
            dtos.add(toDTO(entity));
        }
        return dtos;
    }

    @GetMapping("/{id}")
    public PetDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PetEntity entity = petService.getPet(id);
        return toDTO(entity);
    }

    @PutMapping("/{id}")
    public PetDTO update(@PathVariable Long id, @RequestBody PetDTO dto) throws IllegalOperationException, EntityNotFoundException {

        PetEntity entity = toEntity(dto);
        PetEntity updated = petService.updatePet(id, entity);
        return toDTO(updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
        throws EntityNotFoundException, IllegalOperationException {

        petService.deletePet(id);
    }
    
    private PetEntity toEntity(PetDTO dto) {
        PetEntity entity = new PetEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSpecies(dto.getSpecies());
        entity.setBreed(dto.getBreed());
        entity.setAge(dto.getAge());
        entity.setSex(dto.getSex());
        entity.setSize(dto.getSize());
        entity.setTemperament(dto.getTemperament());
        entity.setSpecialNeeds(dto.getSpecialNeeds());
        entity.setArrivalHistory(dto.getArrivalHistory());
        entity.setStatus(dto.getStatus());

        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }

        return entity;
    }
    private PetDTO toDTO(PetEntity entity) {
        PetDTO dto = new PetDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecies(entity.getSpecies());
        dto.setBreed(entity.getBreed());
        dto.setAge(entity.getAge());
        dto.setSex(entity.getSex());
        dto.setSize(entity.getSize());
        dto.setTemperament(entity.getTemperament());
        dto.setSpecialNeeds(entity.getSpecialNeeds());
        dto.setArrivalHistory(entity.getArrivalHistory());
        dto.setStatus(entity.getStatus());

        if (entity.getShelter() != null) {
            dto.setShelterId(entity.getShelter().getId());
        }

        return dto;
    }
}