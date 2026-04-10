package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.AdoptionHistoryDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionHistoryDetailDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionHistoryService;

@RestController
@RequestMapping("/adoptionhistories")
public class AdoptionHistoryController {

    @Autowired
    private AdoptionHistoryService adoptionHistoryService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdoptionHistoryDetailDTO> findAll() {
        List<AdoptionHistoryEntity> entities = adoptionHistoryService.getAdoptionHistories();
        return modelMapper.map(entities, new TypeToken<List<AdoptionHistoryDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionHistoryDetailDTO findOne(@PathVariable Long id) {
        AdoptionHistoryEntity entity = adoptionHistoryService.getAdoptionHistory(id);
        return modelMapper.map(entity, AdoptionHistoryDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionHistoryDTO create(@RequestBody AdoptionHistoryDTO adoptionHistoryDTO) {
        AdoptionHistoryEntity entity = adoptionHistoryService.createAdoptionHistory(
                modelMapper.map(adoptionHistoryDTO, AdoptionHistoryEntity.class));
        return modelMapper.map(entity, AdoptionHistoryDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdoptionHistoryDTO update(@PathVariable Long id, @RequestBody AdoptionHistoryDTO adoptionHistoryDTO) {
        AdoptionHistoryEntity entity = adoptionHistoryService.updateAdoptionHistory(id,
                modelMapper.map(adoptionHistoryDTO, AdoptionHistoryEntity.class));
        return modelMapper.map(entity, AdoptionHistoryDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        adoptionHistoryService.deleteAdoptionHistory(id);
    }
}