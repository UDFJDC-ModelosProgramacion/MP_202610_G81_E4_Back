package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VaccinationRecordDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/vaccination-records")
public class VaccinationRecordController {

    @PostMapping
    public VaccinationRecordDTO create(@RequestBody VaccinationRecordDTO dto) {
        return dto;
    }

    @GetMapping
    public List<VaccinationRecordDTO> findAll() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public VaccinationRecordDTO findOne(@PathVariable Long id) {
        return new VaccinationRecordDTO();
    }

    @PutMapping("/{id}")
    public VaccinationRecordDTO update(@PathVariable Long id, @RequestBody VaccinationRecordDTO dto) {
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    }
}