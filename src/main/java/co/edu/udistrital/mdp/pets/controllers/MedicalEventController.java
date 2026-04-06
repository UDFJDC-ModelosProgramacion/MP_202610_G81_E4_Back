package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.MedicalEventDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/medical-events")
public class MedicalEventController {

    @PostMapping
    public MedicalEventDTO create(@RequestBody MedicalEventDTO dto) {
        return dto;
    }

    @GetMapping
    public List<MedicalEventDTO> findAll() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public MedicalEventDTO findOne(@PathVariable Long id) {
        return new MedicalEventDTO();
    }

    @PutMapping("/{id}")
    public MedicalEventDTO update(@PathVariable Long id, @RequestBody MedicalEventDTO dto) {
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    }
}