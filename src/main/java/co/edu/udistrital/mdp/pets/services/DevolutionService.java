package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.repositories.DevolutionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DevolutionService {

    @Autowired
    private DevolutionRepository repository;

    /**
     * Crea una devolución verificando que no exista una previa para la misma adopción.
     */
    public DevolutionEntity createDevolution(DevolutionEntity devolution) {
        log.info("Creating devolution for adoption: {}", devolution.getAdoption());

        if (devolution.getAdoption() == null) {
            throw new IllegalArgumentException("Devolution must be linked to an adoption.");
        }
        
        if (devolution.getReason() == null || devolution.getReason().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty.");
        }

        // Validación de duplicados usando el método de tu Repositorio
        List<DevolutionEntity> existing = repository.findByAdoption(devolution.getAdoption());
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("A devolution already exists for this adoption.");
        }

        return repository.save(devolution);
    }

    public DevolutionEntity getDevolution(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devolution not found with id: " + id));
    }

    public List<DevolutionEntity> getDevolutions() {
        return repository.findAll();
    }

    public DevolutionEntity updateDevolution(Long id, DevolutionEntity devolution) {
        DevolutionEntity existing = getDevolution(id);
        
        if (devolution.getReason() == null || devolution.getReason().isEmpty()) {
            throw new IllegalArgumentException("Reason cannot be empty.");
        }

        existing.setReason(devolution.getReason());
        existing.setDetailedDescription(devolution.getDetailedDescription());
        existing.setPetState(devolution.getPetState());
        
        return repository.save(existing);
    }

    public void deleteDevolution(Long id) {
        DevolutionEntity devolution = getDevolution(id);
        repository.delete(devolution);
    }
}