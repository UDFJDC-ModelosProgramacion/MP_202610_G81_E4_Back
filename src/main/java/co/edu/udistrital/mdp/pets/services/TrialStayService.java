package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TrialStayService {
    private static final String NOT_FOUND = "Trial Stay not found";
    private static final String PET_NOT_FOUND = "Pet not found";
    private static final String ADOPTION_NOT_FOUND = "Adoption not found";

    @Autowired private TrialStayRepository trialStayRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private AdoptionRepository adoptionRepository;
    @Autowired
    @Lazy
    private TrialStayService self;

    @Transactional
    public TrialStayEntity createTrialStay(TrialStayEntity trialStay, Long petId, Long adoptionId) {
        trialStay.setPet(petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(PET_NOT_FOUND)));
        var adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException(ADOPTION_NOT_FOUND));
        
        trialStay.setAdoption(adoption);
        adoption.setTrialStay(trialStay); 

        return trialStayRepository.save(trialStay);
    }

    @Transactional(readOnly = true)
    public List<TrialStayEntity> searchTrialStays() { 
        return trialStayRepository.findAll().stream().toList(); 
    }

    @Transactional(readOnly = true)
    public TrialStayEntity searchTrialStay(Long id) {
        return trialStayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(NOT_FOUND));
    }

    @Transactional
    public TrialStayEntity updateTrialStay(Long id, TrialStayEntity details) {
        TrialStayEntity existing = self.searchTrialStay(id);
        
        existing.setStartDate(details.getStartDate());
        existing.setEndDate(details.getEndDate());
        existing.setResult(details.getResult());
        existing.setObservations(details.getObservations());
        return trialStayRepository.save(existing);
    }

    @Transactional
    public void deleteTrialStay(Long id) {
        TrialStayEntity trialStay = trialStayRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOT_FOUND));
        
        AdoptionEntity adoption = trialStay.getAdoption();
        if (adoption != null) {
            adoption.setTrialStay(null);
            adoptionRepository.save(adoption);
        }
        trialStayRepository.delete(trialStay);
    }
}


