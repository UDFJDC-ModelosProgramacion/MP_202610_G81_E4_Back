package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/adoption-requests")
public class AdoptionRequestController {

    @PostMapping
    public AdoptionRequestDTO create(@RequestBody AdoptionRequestDTO dto) {
        return dto;
    }

    @GetMapping
    public List<AdoptionRequestDTO> findAll() {
        return new ArrayList<>();
    }

    @GetMapping("/{id}")
    public AdoptionRequestDTO findOne(@PathVariable Long id) {
        return new AdoptionRequestDTO();
    }

    @PutMapping("/{id}")
    public AdoptionRequestDTO update(@PathVariable Long id, @RequestBody AdoptionRequestDTO dto) {
        return dto;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    }
}