package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate; 
import java.util.List;

@Slf4j
@Service
public class AdoptionTrackingService {

    @Autowired
    private AdoptionTrackingRepository repository;
    public AdoptionTrackingEntity createAdoptionTracking(AdoptionTrackingEntity tracking) {
        log.info("Creating adoption tracking");
        
        if (tracking.getAdoption() == null) {
            throw new IllegalArgumentException("Tracking must have an associated adoption");
        }
        if (tracking.getFrequency() == null || tracking.getFrequency().isEmpty()) {
            throw new IllegalArgumentException("Frequency cannot be null or empty");
        }
        
        return repository.save(tracking);
    }
    public List<AdoptionTrackingEntity> getAdoptionTrackings() {
        log.info("Searching all adoption trackings");
        return repository.findAll();
    }
    public AdoptionTrackingEntity getAdoptionTracking(Long id) {
        log.info("Searching adoption tracking with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption tracking with id " + id + " does not exist"));
    }
    public AdoptionTrackingEntity updateAdoptionTracking(Long id, AdoptionTrackingEntity tracking) {
        log.info("Updating adoption tracking with id: {}", id);
        AdoptionTrackingEntity existing = getAdoptionTracking(id);
        
        if (tracking.getNextReview() == null) {
            throw new IllegalArgumentException("Next review date cannot be null");
        }
        existing.setFrequency(tracking.getFrequency());
        existing.setNotes(tracking.getNotes());
        existing.setNextReview(tracking.getNextReview());
        
        return repository.save(existing);
    }
    public void deleteAdoptionTracking(Long id) {
        log.info("Deleting adoption tracking with id: {}", id);
        AdoptionTrackingEntity tracking = getAdoptionTracking(id);
        repository.delete(tracking);
    }
}