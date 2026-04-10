package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDetailDTO;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody VeterinarianDTO dto) {
        try {
            VeterinarianEntity entity = toEntity(dto);
            return new ResponseEntity<>(new VeterinarianDTO(service.createVeterinarian(entity)), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear: " + e.getMessage());
        }
    }

    @GetMapping
    public List<VeterinarianDTO> findAll() {
        List<VeterinarianDTO> list = new ArrayList<>();
        for (VeterinarianEntity v : service.searchVeterinarians()) {
            list.add(new VeterinarianDTO(v));
        }
        return list;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody VeterinarianDTO dto) {
        try {
            VeterinarianEntity entity = toEntity(dto);
            return ResponseEntity.ok(new VeterinarianDTO(service.updateVeterinarian(id, entity)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.deleteVeterinarian(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private VeterinarianEntity toEntity(VeterinarianDTO dto) {
        if (dto == null) return null;
        VeterinarianEntity e = new VeterinarianEntity();
        e.setId(dto.getId());
        e.setVeterinarianIdBusiness(dto.getVeterinarianIdBusiness());
        e.setLastName(dto.getLastName());
        e.setSpecialties(dto.getSpecialties());
        e.setAvailability(dto.getAvailability());

        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            e.setShelter(shelter);
        }
        return e;
    }
}
