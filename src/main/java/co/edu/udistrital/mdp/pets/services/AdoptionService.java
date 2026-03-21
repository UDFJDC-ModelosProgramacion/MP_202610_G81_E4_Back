package co.edu.udistrital.mdp.pets.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionService {
    @Autowired
    private AdoptionRepository adoptionRepository;
    public AdoptionEntity createAdoption (AdoptionEntity adoption){
        log.info("Creating Adoption");
        if (adoption == null){
            throw new IllegalArgumentException("Adoption cannot be null");
        }
        if (adoption.getPet() == null){
            throw new IllegalArgumentException("Adopted pet cannot be null");
        }
        if (!adoption.getPet().getStatus().equals("AVAILABLE")){
            throw new IllegalStateException("Pet is not available for adoption");
        }
        if (adoption.getAdoptionDate()==null){
            throw new IllegalArgumentException("Adoption date cannot be null");
        }
        return adoptionRepository.save(adoption);
    }
    public AdoptionEntity searchAdoption(Long id) {
        log.info("Searching Adoption with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
    }
     public List<AdoptionEntity> searchAdoptions() {
        log.info("Searching all adoptions");
        return adoptionRepository.findAll();
    }
    public AdoptionEntity updateAdoption(Long id, AdoptionEntity adoption) {
        log.info("Updating Adoption with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (adoption == null) {
            throw new IllegalArgumentException("Adoption cannot be null");
        }
        AdoptionEntity existing = adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
        existing.setStatus(adoption.getStatus());
        return adoptionRepository.save(existing);
    }
    public void deleteAdoption(Long id) {
        log.info("Deleting Adoption with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        AdoptionEntity adoption = adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
        if (adoption.getStatus() == null || !adoption.getStatus().equals("FINISHED")) {
            throw new IllegalStateException("Adoption process has not finished");
        }
        adoptionRepository.delete(adoption);
    }
}

    

