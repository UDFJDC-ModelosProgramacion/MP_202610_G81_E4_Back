package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
public class AdoptionHistoryService {

    @Autowired
    private AdoptionHistoryRepository repository;

    public AdoptionHistoryEntity createAdoptionHistory(AdoptionHistoryEntity history) {
        log.info("Creating adoption history");
        if (history.getAdoption() == null)
            throw new IllegalArgumentException("History must have an associated adoption");
        if (history.getReason() == null || history.getReason().isEmpty())
            throw new IllegalArgumentException("Reason cannot be null or empty");
        
        return repository.save(history);
    }

    public List<AdoptionHistoryEntity> getAdoptionHistories() {
        log.info("Searching all adoption histories");
        return repository.findAll();
    }

    public AdoptionHistoryEntity getAdoptionHistory(Long id) {
        log.info("Searching adoption history with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adoption history with id " + id + " does not exist"));
    }

    public AdoptionHistoryEntity updateAdoptionHistory(Long id, AdoptionHistoryEntity history) {
        log.info("Updating adoption history with id: {}", id);
        AdoptionHistoryEntity existing = getAdoptionHistory(id);
        
        if (history.getReason() == null || history.getReason().isEmpty())
            throw new IllegalArgumentException("Reason cannot be null or empty");

        existing.setReason(history.getReason());
        existing.setDetail(history.getDetail());
        existing.setDate(history.getDate());
        existing.setAdoption(history.getAdoption());
        
        return repository.save(existing);
    }

    public void deleteAdoptionHistory(Long id) {
        log.info("Deleting adoption history with id: {}", id);
        AdoptionHistoryEntity history = getAdoptionHistory(id);
        repository.delete(history);
    }
}