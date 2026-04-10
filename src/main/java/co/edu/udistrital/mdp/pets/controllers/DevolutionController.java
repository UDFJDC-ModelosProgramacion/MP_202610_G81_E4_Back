package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.DevolutionDTO;
import co.edu.udistrital.mdp.pets.dto.DevolutionDetailDTO;
import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.services.DevolutionService;

@RestController
@RequestMapping("/devolutions")
public class DevolutionController {

    @Autowired
    private DevolutionService devolutionService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DevolutionDetailDTO> findAll() {
        List<DevolutionEntity> entities = devolutionService.getDevolutions();
        return modelMapper.map(entities, new TypeToken<List<DevolutionDetailDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DevolutionDetailDTO findOne(@PathVariable Long id) {
        DevolutionEntity entity = devolutionService.getDevolution(id);
        return modelMapper.map(entity, DevolutionDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DevolutionDTO create(@RequestBody DevolutionDTO devolutionDTO) {
        DevolutionEntity entity = devolutionService.createDevolution(
                modelMapper.map(devolutionDTO, DevolutionEntity.class));
        return modelMapper.map(entity, DevolutionDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DevolutionDTO update(@PathVariable Long id, @RequestBody DevolutionDTO devolutionDTO) {
        DevolutionEntity entity = devolutionService.updateDevolution(id,
                modelMapper.map(devolutionDTO, DevolutionEntity.class));
        return modelMapper.map(entity, DevolutionDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        devolutionService.deleteDevolution(id);
    }
}