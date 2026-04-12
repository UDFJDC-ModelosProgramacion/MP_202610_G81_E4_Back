package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionRequestService {
    private static final String REQUEST_NOT_FOUND = "Adoption request not found with id: ";

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public AdoptionRequestEntity createAdoptionRequest(AdoptionRequestEntity request)
            throws IllegalOperationException {

        log.info("Inicia creación de solicitud");

        if (request.getAdopter() == null)
            throw new IllegalOperationException("Adopter is required");

        if (request.getPet() == null)
            throw new IllegalOperationException("Pet is required");

        if (request.getStatus() == null || request.getStatus().isEmpty())
            throw new IllegalOperationException("Status is invalid");

        AdopterEntity adopter = adopterRepository.findById(request.getAdopter().getId())
                .orElseThrow(() -> new IllegalOperationException("Adopter does not exist"));

        PetEntity pet = petRepository.findById(request.getPet().getId())
                .orElseThrow(() -> new IllegalOperationException("Pet does not exist"));

        request.setAdopter(adopter);
        request.setPet(pet);

        return adoptionRequestRepository.save(request);
    }

   @Transactional(readOnly = true)
    public List<AdoptionRequestEntity> getRequests() {
        return adoptionRequestRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public AdoptionRequestEntity getRequest(Long id) throws EntityNotFoundException {
        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(REQUEST_NOT_FOUND + id));
    }

    @Transactional
    public AdoptionRequestEntity updateAdoptionRequest(Long id, AdoptionRequestEntity request)
            throws EntityNotFoundException, IllegalOperationException {

        if (!adoptionRequestRepository.existsById(id))
            throw new EntityNotFoundException(REQUEST_NOT_FOUND);

        if (request.getStatus() == null || request.getStatus().isEmpty())
            throw new IllegalOperationException("Status is invalid");

        request.setId(id);
        return adoptionRequestRepository.save(request);
    }

    @Transactional
    public void deleteRequest(Long id) throws EntityNotFoundException {
        log.info("Deleting request: {}", id);
        if (!adoptionRequestRepository.existsById(id))
            throw new EntityNotFoundException(REQUEST_NOT_FOUND + id);
        adoptionRequestRepository.deleteById(id);
    }
}
