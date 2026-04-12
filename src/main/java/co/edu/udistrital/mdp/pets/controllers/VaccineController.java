package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VaccineDTO;
import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.services.VaccineService;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vaccines")
public class VaccineController {

    private static final String ERR_MSG = "message"; 

    @Autowired
    private VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody VaccineDTO dto) {
        try {
            VaccineEntity saved = vaccineService.createVaccine(toEntity(dto));
            return new ResponseEntity<>(new VaccineDTO(saved), HttpStatus.CREATED);
        } catch (IllegalOperationException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<VaccineDTO>> getAll() {
        List<VaccineDTO> list = vaccineService.getVaccines().stream()
                .map(VaccineDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new VaccineDTO(vaccineService.getVaccine(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody VaccineDTO dto) {
        try {
            VaccineEntity updated = vaccineService.updateVaccine(id, toEntity(dto));
            return ResponseEntity.ok(new VaccineDTO(updated));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        } catch (IllegalOperationException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            vaccineService.deleteVaccine(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    private VaccineEntity toEntity(VaccineDTO dto) {
        if (dto == null) return null;
        
        VaccineEntity v = new VaccineEntity();
        v.setId(dto.getId());
        v.setVaccineName(dto.getVaccineName());
        v.setApplicationDate(dto.getApplicationDate());
        v.setNextApplicationDate(dto.getNextApplicationDate());
        v.setBatchNumber(dto.getBatchNumber());
        v.setObservations(dto.getObservations());
        
        if (dto.getVaccinationRecordId() != null) {
            VaccinationRecordEntity vRecord = new VaccinationRecordEntity();
            vRecord.setId(dto.getVaccinationRecordId());
            v.setVaccinationRecord(vRecord);
        }
        
        return v;
    }
}
