package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/pets")
public class PetController {

    @PostMapping
    public PetDTO create(@RequestBody PetDTO dto) {
        return dto;
    }

    @GetMapping
    public List<PetDTO> findAll() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public PetDTO findOne(@PathVariable Long id) {
        return new PetDTO();
    }

    @PutMapping("/{id}")
    public PetDTO update(@PathVariable Long id, @RequestBody PetDTO dto) {
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    }
}