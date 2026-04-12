package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterDetailDTO;
import co.edu.udistrital.mdp.pets.entities.Adopter;
import co.edu.udistrital.mdp.pets.services.AdopterService;

@RestController
@RequestMapping("/adopters")
public class AdopterController {
    
    @Autowired
    private AdopterService adopterService;
    
    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody AdopterDTO dto) {
    try {
        Adopter adopter = toEntity(dto);
        Adopter saved = adopterService.createAdopter(adopter);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(e.getMessage());
    }
}
    
    @GetMapping
    public ResponseEntity<List<AdopterDTO>> findAll() {
        List<AdopterDTO> adopters = adopterService.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(adopters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopterDetailDTO> findOne(@PathVariable Long id) {
        return adopterService.findById(id)
            .map(this::toDetailDTO)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AdopterDTO> update(@PathVariable Long id, @RequestBody AdopterDTO dto) {
        try {
            Adopter updated = adopterService.updateAdopter(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
            } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
        }
    }
    
    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            adopterService.deleteAdopter(id);
                return ResponseEntity.noContent().build();
             } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
        }
    }   
    
    // MAPPERS
    private AdopterDTO toDTO(Adopter entity) {
        return new AdopterDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getLastName(),
            entity.getAddress(),
            entity.getHousingType(),
            entity.getHasChildren(),
            entity.getHasOtherPets()
        );
    }
    
    private AdopterDetailDTO toDetailDTO(Adopter entity) {
        return new AdopterDetailDTO(
            entity.getId(),
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getLastName(),
            entity.getAddress(),
            entity.getHousingType(),
            entity.getHasChildren(),
            entity.getHasOtherPets(),
            entity.getPreferences()
        );
    }
    
    private Adopter toEntity(AdopterDTO dto) {
        Adopter adopter = new Adopter();
        adopter.setName(dto.getName());
        adopter.setEmail(dto.getEmail());
        adopter.setPhone(dto.getPhone());
        adopter.setLastName(dto.getLastName());
        adopter.setAddress(dto.getAddress());
        adopter.setHousingType(dto.getHousingType());
        adopter.setHasChildren(dto.getHasChildren());
        adopter.setHasOtherPets(dto.getHasOtherPets());
        return adopter;
    }
}