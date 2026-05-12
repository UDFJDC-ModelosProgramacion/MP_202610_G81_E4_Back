package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.AdoptionTrackingDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionTrackingDetailDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionTrackingService;

@RestController
@RequestMapping("/adoptiontrackings")
public class AdoptionTrackingController {

    @Autowired
    private AdoptionTrackingService adoptionTrackingService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdoptionTrackingDetailDTO> findAll() {
        List<AdoptionTrackingEntity> entities = adoptionTrackingService.getAdoptionTrackings();
        return modelMapper.map(entities, new TypeToken<List<AdoptionTrackingDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionTrackingDetailDTO findOne(@PathVariable Long id) {
        AdoptionTrackingEntity entity = adoptionTrackingService.getAdoptionTracking(id);
        return modelMapper.map(entity, AdoptionTrackingDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionTrackingDTO create(@RequestBody AdoptionTrackingDTO adoptionTrackingDTO) {
        AdoptionTrackingEntity entity = adoptionTrackingService.createAdoptionTracking(
                modelMapper.map(adoptionTrackingDTO, AdoptionTrackingEntity.class));
        return modelMapper.map(entity, AdoptionTrackingDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionTrackingDTO update(@PathVariable Long id, @RequestBody AdoptionTrackingDTO adoptionTrackingDTO) {
        AdoptionTrackingEntity entity = adoptionTrackingService.updateAdoptionTracking(id,
                modelMapper.map(adoptionTrackingDTO, AdoptionTrackingEntity.class));
        return modelMapper.map(entity, AdoptionTrackingDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adoptionTrackingService.deleteAdoptionTracking(id);
    }
}