package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    private static final String ERR_MSG = "message";

    @Autowired
    private VeterinarianService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody VeterinarianDTO dto) {
        try {
            VeterinarianEntity entity = toEntity(dto);
            return new ResponseEntity<>(new VeterinarianDTO(service.createVeterinarian(entity)), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, "Error al crear: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<VeterinarianDTO>> findAll() {
        // Solución Sonar: Uso de Stream y .toList() para inmutabilidad
        List<VeterinarianDTO> list = service.searchVeterinarians().stream()
                .map(VeterinarianDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody VeterinarianDTO dto) {
        try {
            VeterinarianEntity entity = toEntity(dto);
            return ResponseEntity.ok(new VeterinarianDTO(service.updateVeterinarian(id, entity)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, "Error al actualizar: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteVeterinarian(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
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
