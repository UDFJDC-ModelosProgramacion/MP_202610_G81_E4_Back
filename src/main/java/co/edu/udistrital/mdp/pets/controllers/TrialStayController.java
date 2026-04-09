package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.TrialStayDTO;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.services.TrialStayService;

@RestController
@RequestMapping("/trial-stays") 
public class TrialStayController {

    @Autowired
    private TrialStayService trialStayService;

    @GetMapping
    public ResponseEntity<List<TrialStayDTO>> getAllTrialStays() {
        List<TrialStayDTO> list = trialStayService.searchTrialStays()
                .stream()
                .map(TrialStayDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrialStayDTO> getTrialStayById(@PathVariable Long id) {
        TrialStayEntity entity = trialStayService.searchTrialStay(id);
        return ResponseEntity.ok(new TrialStayDTO(entity));
    }

    @PostMapping
    public ResponseEntity<TrialStayDTO> createTrialStay(@RequestBody TrialStayDTO dto) {
        TrialStayEntity entity = new TrialStayEntity();
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setResult(dto.getResult());
        entity.setObservations(dto.getObservations());

        // El service se encarga de buscar Pet y Adoption por ID
        TrialStayEntity saved = trialStayService.createTrialStay(
                entity, 
                dto.getPetId(), 
                dto.getAdoptionId()
        );
        
        return new ResponseEntity<>(new TrialStayDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrialStayDTO> updateTrialStay(@PathVariable Long id, @RequestBody TrialStayDTO dto) {
        TrialStayEntity details = new TrialStayEntity();
        // Importante: No seteamos el ID aquí, el Service lo usa para buscar el existente
        details.setStartDate(dto.getStartDate());
        details.setEndDate(dto.getEndDate());
        details.setResult(dto.getResult());
        details.setObservations(dto.getObservations());

        TrialStayEntity updated = trialStayService.updateTrialStay(id, details);
        return ResponseEntity.ok(new TrialStayDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrialStay(@PathVariable Long id) {
        trialStayService.deleteTrialStay(id);
        return ResponseEntity.noContent().build();
    }
}
