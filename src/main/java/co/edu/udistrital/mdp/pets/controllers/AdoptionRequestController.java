package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/adoption-requests")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService service;

    @PostMapping
    public AdoptionRequestDTO create(@RequestBody AdoptionRequestDTO dto)
            throws IllegalOperationException {

        AdoptionRequestEntity entity = toEntity(dto);
        return toDTO(service.createAdoptionRequest(entity));
    }

    @GetMapping
    public List<AdoptionRequestDTO> findAll() {
        List<AdoptionRequestDTO> list = new ArrayList<>();
        for (AdoptionRequestEntity e : service.getAdoptionRequests()) {
            list.add(toDTO(e));
        }
        return list;
    }

    @GetMapping("/{id}")
    public AdoptionRequestDTO findOne(@PathVariable Long id)
            throws EntityNotFoundException {

        return toDTO(service.getAdoptionRequest(id));
    }

    @PutMapping("/{id}")
    public AdoptionRequestDTO update(@PathVariable Long id, @RequestBody AdoptionRequestDTO dto)
            throws EntityNotFoundException, IllegalOperationException {

        AdoptionRequestEntity entity = toEntity(dto);
        return toDTO(service.updateAdoptionRequest(id, entity));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
            throws EntityNotFoundException {

        service.deleteAdoptionRequest(id);
    }

    private AdoptionRequestEntity toEntity(AdoptionRequestDTO dto) {
        AdoptionRequestEntity e = new AdoptionRequestEntity();

        e.setId(dto.getId());
        e.setRequestDate(dto.getRequestDate());
        e.setStatus(dto.getStatus());
        e.setMotivation(dto.getMotivation());

        if (dto.getPetId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPetId());
            e.setPet(pet);
        }

        if (dto.getAdopterId() != null) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setId(dto.getAdopterId());
            e.setAdopter(adopter);
        }

        return e;
    }

    private AdoptionRequestDTO toDTO(AdoptionRequestEntity e) {
        AdoptionRequestDTO dto = new AdoptionRequestDTO();

        dto.setId(e.getId());
        dto.setRequestDate(e.getRequestDate());
        dto.setStatus(e.getStatus());
        dto.setMotivation(e.getMotivation());

        if (e.getPet() != null) {
            dto.setPetId(e.getPet().getId());
        }

        if (e.getAdopter() != null) {
            dto.setAdopterId(e.getAdopter().getId());
        }

        return dto;
    }
}