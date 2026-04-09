package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TrialStayService {
    @Autowired private TrialStayRepository trialStayRepository;
    @Autowired private PetRepository petRepository;
    @Autowired private AdoptionRepository adoptionRepository;

    @Transactional
    public TrialStayEntity createTrialStay(TrialStayEntity trialStay, Long petId, Long adoptionId) {
        trialStay.setPet(petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found")));
        var adoption = adoptionRepository.findById(adoptionId)
                .orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
        
        trialStay.setAdoption(adoption);
        adoption.setTrialStay(trialStay); 

        return trialStayRepository.save(trialStay);
    }

    @Transactional(readOnly = true)
    public List<TrialStayEntity> searchTrialStays() { return trialStayRepository.findAll(); }

    @Transactional(readOnly = true)
    public TrialStayEntity searchTrialStay(Long id) {
        return trialStayRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    @Transactional
    public TrialStayEntity updateTrialStay(Long id, TrialStayEntity details) {
        TrialStayEntity existing = searchTrialStay(id);
        existing.setStartDate(details.getStartDate());
        existing.setEndDate(details.getEndDate());
        existing.setResult(details.getResult());
        existing.setObservations(details.getObservations());
        return trialStayRepository.save(existing);
    }

    @Transactional
    public void deleteTrialStay(Long id) {
    TrialStayEntity trialStay = trialStayRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Trial Stay not found"));
    AdoptionEntity adoption = trialStay.getAdoption();
    if (adoption != null) {
        adoption.setTrialStay(null);
        adoptionRepository.save(adoption);
    }
    trialStayRepository.delete(trialStay);
}
}


