package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VaccinationRecordDTO;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VaccinationRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/vaccination-records")
public class VaccinationRecordController {

    @Autowired
    private VaccinationRecordService service;

    @PostMapping
    public VaccinationRecordDTO create(@RequestBody VaccinationRecordDTO dto)
            throws IllegalOperationException {

        VaccinationRecordEntity entity = toEntity(dto);
        return toDTO(service.createRecord(entity));
    }

    @GetMapping
    public List<VaccinationRecordDTO> findAll() {
        List<VaccinationRecordDTO> list = new ArrayList<>();
        for (VaccinationRecordEntity e : service.getRecords()) {
            list.add(toDTO(e));
        }
        return list;
    }

    private VaccinationRecordEntity toEntity(VaccinationRecordDTO dto) {
        VaccinationRecordEntity e = new VaccinationRecordEntity();

        e.setId(dto.getId());

        if (dto.getPetId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPetId());
            e.setPet(pet);
        }

        return e;
    }

    private VaccinationRecordDTO toDTO(VaccinationRecordEntity e) {
        VaccinationRecordDTO dto = new VaccinationRecordDTO();

        dto.setId(e.getId());

        if (e.getPet() != null) {
            dto.setPetId(e.getPet().getId());
        }

        return dto;
    }
}