package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionTrackingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdoptionTrackingService {
    @Autowired
    private AdoptionTrackingRepository repository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Transactional
    public AdoptionTrackingEntity createAdoptionTracking(AdoptionTrackingEntity tracking) {
        if (tracking == null) {
            throw new IllegalArgumentException("Tracking cannot be null");
        }
        
        if (tracking.getAdoption() == null || tracking.getAdoption().getId() == null) {
            throw new IllegalArgumentException("Tracking must be associated with an existing adoption.");
        }

        adoptionRepository.findById(tracking.getAdoption().getId())
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found with ID: " + tracking.getAdoption().getId()));
        
        return repository.save(tracking);
    }

    @Transactional(readOnly = true)
    public List<AdoptionTrackingEntity> getAdoptionTrackings() {
        // Solución Sonar: Uso de .toList() para inmutabilidad
        return repository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public AdoptionTrackingEntity getAdoptionTracking(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found with ID: " + id));
    }

    @Transactional
    public AdoptionTrackingEntity updateAdoptionTracking(Long id, AdoptionTrackingEntity tracking) {
        AdoptionTrackingEntity existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found with ID: " + id));
        
        if (tracking.getNextReview() == null) {
            throw new IllegalArgumentException("Next review date cannot be null");
        }
        
        if (tracking.getFrequency() != null) existing.setFrequency(tracking.getFrequency());
        if (tracking.getNotes() != null) existing.setNotes(tracking.getNotes());
        existing.setNextReview(tracking.getNextReview());
        
        return repository.save(existing);
    }

    @Transactional
    public void deleteAdoptionTracking(Long id) {
        AdoptionTrackingEntity tracking = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found with ID: " + id));
                
        repository.delete(tracking);
    }
}