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
        if (trialStay.getAdoption() == null) {
            throw new IllegalArgumentException("Adoption process cannot be null");
        }
        return trialStayRepository.save(trialStay);
    }
    public TrialStayEntity searchTrialStay(Long id) {
        log.info("Searching TrialStay with id: {}", id);
        return trialStayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trial Stay not found"));
    }
    public List<TrialStayEntity> searchAllTrialStays() {
        log.info("Searching all TrialStays");
        return trialStayRepository.findAll();
    }
    public TrialStayEntity updateTrialStay(Long id, TrialStayEntity trialStay) {
        log.info("Updating TrialStay with id: {}", id);

        TrialStayEntity existing = trialStayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial Stay not found"));
        if (trialStay.getStartDate() == null || trialStay.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        existing.setStartDate(trialStay.getStartDate());
        existing.setEndDate(trialStay.getEndDate());
        existing.setResult(trialStay.getResult());
        existing.setObservations(trialStay.getObservations());
        existing.setPet(trialStay.getPet());
        existing.setAdoption(trialStay.getAdoption());
        return trialStayRepository.save(existing);
    }
    public void deleteTrialStay(Long id) {
        log.info("Deleting TrialStay with id: {}", id);
        TrialStayEntity trialStay = trialStayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Trial Stay not found"));
        if (trialStay.getAdoption() != null) {
            throw new IllegalStateException("Cannot delete TrialStay with an associated adoption process");
        }
        trialStayRepository.delete(trialStay);
    }
}


