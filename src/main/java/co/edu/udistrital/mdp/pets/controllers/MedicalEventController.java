package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.MedicalEventDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.*;
import co.edu.udistrital.mdp.pets.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/medical-events")
public class MedicalEventController {

    @Autowired
    private MedicalEventService service;

    @PostMapping
    public MedicalEventDTO create(@RequestBody MedicalEventDTO dto)
            throws IllegalOperationException {

        MedicalEventEntity entity = toEntity(dto);
        return toDTO(service.createMedicalEvent(entity));
    }

    @GetMapping
    public List<MedicalEventDTO> findAll() {
        List<MedicalEventDTO> list = new ArrayList<>();
        for (MedicalEventEntity e : service.getMedicalEvents()) {
            list.add(toDTO(e));
        }
        return list;
    }

    @GetMapping("/{id}")
    public MedicalEventDTO findOne(@PathVariable Long id)
            throws EntityNotFoundException {

        return toDTO(service.getMedicalEvent(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
            throws EntityNotFoundException {

        service.deleteMedicalEvent(id);
    }

    private MedicalEventEntity toEntity(MedicalEventDTO dto) {
        MedicalEventEntity e = new MedicalEventEntity();

        e.setId(dto.getId());
        e.setEventType(dto.getEventType());
        e.setEventDate(dto.getEventDate());
        e.setDescription(dto.getDescription());
        e.setDiagnosis(dto.getDiagnosis());
        e.setTreatment(dto.getTreatment());
        e.setAttachments(dto.getAttachments());

        if (dto.getPetId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPetId());
            e.setPet(pet);
        }

        return e;
    }

    private MedicalEventDTO toDTO(MedicalEventEntity e) {
        MedicalEventDTO dto = new MedicalEventDTO();

        dto.setId(e.getId());
        dto.setEventType(e.getEventType());
        dto.setEventDate(e.getEventDate());
        dto.setDescription(e.getDescription());
        dto.setDiagnosis(e.getDiagnosis());
        dto.setTreatment(e.getTreatment());
        dto.setAttachments(e.getAttachments());

        if (e.getPet() != null) {
            dto.setPetId(e.getPet().getId());
        }

        return dto;
    }
}