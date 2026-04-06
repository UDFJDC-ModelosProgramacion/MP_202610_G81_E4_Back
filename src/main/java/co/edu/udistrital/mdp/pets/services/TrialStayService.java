package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.repositories.TrialStayRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrialStayService {

    @Autowired
    private TrialStayRepository trialStayRepository;

    public TrialStayEntity createTrialStay(TrialStayEntity trialStay) {

        if (trialStay == null) {
            throw new IllegalArgumentException("TrialStay cannot be null");
        }

        if (trialStay.getStartDate() == null || trialStay.getEndDate() == null) {
            throw new IllegalArgumentException("Start and end date are required");
        }

        if (trialStay.getStartDate().isAfter(trialStay.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (trialStay.getPet() == null || trialStay.getAdoption() == null) {
            throw new IllegalArgumentException("Pet and Adoption are required");
        }

        return trialStayRepository.save(trialStay);
    }

    public TrialStayEntity searchTrialStay(Long id) {
        return trialStayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("TrialStay not found"));
    }

    public List<TrialStayEntity> searchAllTrialStays() {
        return trialStayRepository.findAll();
    }

    public TrialStayEntity updateTrialStay(Long id, TrialStayEntity trialStay) {

        TrialStayEntity existing = searchTrialStay(id);

        existing.setStartDate(trialStay.getStartDate());
        existing.setEndDate(trialStay.getEndDate());
        existing.setResult(trialStay.getResult());
        existing.setObservations(trialStay.getObservations());

        if (trialStay.getPet() != null) {
            existing.setPet(trialStay.getPet());
        }

        if (trialStay.getAdoption() != null) {
            existing.setAdoption(trialStay.getAdoption());
        }

        return trialStayRepository.save(existing);
    }

    public void deleteTrialStay(Long id) {
        TrialStayEntity entity = searchTrialStay(id);
        trialStayRepository.delete(entity);
    }
}


