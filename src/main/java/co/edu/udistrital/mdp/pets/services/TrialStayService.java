package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.TrialStayRepository;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;

@Slf4j
@Service
public class TrialStayService {

    @Autowired
    private TrialStayRepository trialStayRepository;

    public TrialStayEntity createTrialStay(TrialStayEntity trialStay) {
        log.info("Creating TrialStay");
        if (trialStay == null) {
            throw new IllegalArgumentException("TrialStay cannot be null");
        }
        if (trialStay.getStartDate() == null || trialStay.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if (trialStay.getStartDate().isAfter(trialStay.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        
        TrialStayEntity savedTrialStay = trialStayRepository.save(trialStay);
        log.info("TrialStay created with id: {}", savedTrialStay.getId());
        return savedTrialStay;
    }

    public TrialStayEntity searchTrialStay(Long id) {
        log.info("Searching TrialStay with id: {}", id);
        return trialStayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial Stay not found with id: " + id));
    }

    public List<TrialStayEntity> searchAllTrialStays() {
        log.info("Searching all TrialStays");
        return trialStayRepository.findAll();
    }

    public TrialStayEntity updateTrialStay(Long id, TrialStayEntity trialStay) {
        log.info("Updating TrialStay with id: {}", id);
        TrialStayEntity existing = trialStayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe la estancia con id: " + id));
        
        existing.setStartDate(trialStay.getStartDate());
        existing.setEndDate(trialStay.getEndDate());
        existing.setResult(trialStay.getResult());
        existing.setObservations(trialStay.getObservations());
        if (trialStay.getPet() != null) existing.setPet(trialStay.getPet());
        if (trialStay.getAdoption() != null) existing.setAdoption(trialStay.getAdoption());

        return trialStayRepository.save(existing);
    }

    public void deleteTrialStay(Long id) {
        log.info("Deleting TrialStay with id: {}", id);
        TrialStayEntity trialStay = trialStayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trial Stay not found"));
        trialStayRepository.delete(trialStay);
    }
}


