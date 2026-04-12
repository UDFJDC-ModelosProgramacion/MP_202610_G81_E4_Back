package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VaccineDTO;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VaccineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/vaccines")
public class VaccineController {
    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public VaccineDTO create(@RequestBody VaccineDTO dto) throws IllegalOperationException {
        VaccineEntity entity = toEntity(dto);
        VaccineEntity newVaccine = vaccineService.createVaccine(entity);
        return toDTO(newVaccine);
    }

    @GetMapping
    public List<VaccineDTO> findAll() {
        List<VaccineEntity> entities = vaccineService.getVaccines();
        List<VaccineDTO> dtos = new ArrayList<>();

        for (VaccineEntity entity : entities) {
            dtos.add(toDTO(entity));
        }

        return dtos;
    }

    private VaccineEntity toEntity (VaccineDTO dto){
        VaccineEntity entity = new VaccineEntity();
        entity.setId(dto.getId());
        entity.setVaccineName(dto.getVaccineName());
        if (dto.getVaccinationRecordId() != null) {
            VaccinationRecordEntity record = new VaccinationRecordEntity();
            record.setId(dto.getVaccinationRecordId());
            entity.setVaccinationRecord(record);
        }
        return entity;
    }

    private VaccineDTO toDTO(VaccineEntity entity){
        VaccineDTO dto = new VaccineDTO();
        dto.setId(entity.getId());
        dto.setVaccineName(entity.getVaccineName());
        if (entity.getVaccinationRecord() != null) {
            dto.setVaccinationRecordId(entity.getVaccinationRecord().getId());
        }
        return dto;
    }
}
