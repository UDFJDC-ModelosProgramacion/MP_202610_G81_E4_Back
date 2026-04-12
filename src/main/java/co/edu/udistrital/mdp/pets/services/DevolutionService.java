package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.DevolutionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DevolutionService {

    @Autowired
    private DevolutionRepository repository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Transactional
    public DevolutionEntity create(DevolutionEntity devolution) {
        if (devolution == null) throw new IllegalArgumentException("Devolution cannot be null");
        
        if (devolution.getAdoption() == null || devolution.getAdoption().getId() == null) {
            throw new IllegalArgumentException("Devolution must be associated with an existing adoption.");
        }
        AdoptionEntity adoption = adoptionRepository.findById(devolution.getAdoption().getId())
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found with ID: " + devolution.getAdoption().getId()));
        
        devolution.setAdoption(adoption);
        return repository.save(devolution);
    }

    @Transactional(readOnly = true)
    public List<DevolutionEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public DevolutionEntity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Devolution not found with ID: " + id));
    }

    @Transactional
    public DevolutionEntity update(Long id, DevolutionEntity devolution) {
        DevolutionEntity existing = findById(id);
        if (devolution.getReturnDate() == null) {
            throw new IllegalArgumentException("Return date cannot be null");
        }
        
        existing.setReturnDate(devolution.getReturnDate());
        if (devolution.getReason() != null) existing.setReason(devolution.getReason());
        if (devolution.getDetailedDescription() != null) existing.setDetailedDescription(devolution.getDetailedDescription());
        if (devolution.getPetState() != null) existing.setPetState(devolution.getPetState());
        
        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        DevolutionEntity devolution = findById(id);
        repository.delete(devolution);
    }
}