package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.entities.Veterinarian;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @PostMapping
    public ResponseEntity<VeterinarianDTO> create(@RequestBody VeterinarianDTO dto) {
        Veterinarian v = toEntity(dto);
        Veterinarian saved = veterinarianService.createVetterinarian(v);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarianDTO> update(@PathVariable Long id, @RequestBody VeterinarianDTO dto) {
        Veterinarian updated = veterinarianService.updateVetterinarian(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        veterinarianService.deleteVetterinarian(id);
        return ResponseEntity.noContent().build();
    }

    private VeterinarianDTO toDTO(Veterinarian v) {
        return new VeterinarianDTO(
            v.getId(),
            v.getName(),
            v.getEmail(),
            v.getPhone(),
            v.getLastName(),
            v.getAvailability(),
            v.getShelterId()
        );
    }

    private Veterinarian toEntity(VeterinarianDTO dto) {
        Veterinarian v = new Veterinarian();
        v.setName(dto.getName());
        v.setEmail(dto.getEmail());
        v.setPhone(dto.getPhone());
        v.setLastName(dto.getLastName());
        v.setAvailability(dto.getAvailability());
        v.setShelterId(dto.getShelterId());
        return v;
    }
}