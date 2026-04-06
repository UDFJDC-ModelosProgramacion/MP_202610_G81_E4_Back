package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VaccineDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/vaccines")
public class VaccineController {

    @PostMapping
    public VaccineDTO create(@RequestBody VaccineDTO dto) {
        return dto;
    }

    @GetMapping
    public List<VaccineDTO> findAll() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public VaccineDTO findOne(@PathVariable Long id) {
        return new VaccineDTO();
    }

    @PutMapping("/{id}")
    public VaccineDTO update(@PathVariable Long id, @RequestBody VaccineDTO dto) {
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    }
}
