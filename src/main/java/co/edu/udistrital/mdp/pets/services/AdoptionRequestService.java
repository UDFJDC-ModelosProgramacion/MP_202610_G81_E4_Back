package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionRequestService {

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

        Optional<AdopterEntity> adopter = adopterRepository.findById(request.getAdopter().getId());
        if (adopter.isEmpty())
            throw new IllegalOperationException("Adopter does not exist");

        Optional<PetEntity> pet = petRepository.findById(request.getPet().getId());
        if (pet.isEmpty())
            throw new IllegalOperationException("Pet does not exist");

        request.setAdopter(adopter.get());
        request.setPet(pet.get());

        return adoptionRequestRepository.save(request);
    }

    @Transactional
    public List<AdoptionRequestEntity> getAdoptionRequests() {
        return adoptionRequestRepository.findAll();
    }

    @Transactional
    public AdoptionRequestEntity getAdoptionRequest(Long id) throws EntityNotFoundException {
        Optional<AdoptionRequestEntity> entity = adoptionRequestRepository.findById(id);
        if (entity.isEmpty())
            throw new EntityNotFoundException("Request not found");
        return entity.get();
    }

    @Transactional
    public AdoptionRequestEntity updateAdoptionRequest(Long id, AdoptionRequestEntity request)
            throws EntityNotFoundException, IllegalOperationException {

        Optional<AdoptionRequestEntity> existing = adoptionRequestRepository.findById(id);
        if (existing.isEmpty())
            throw new EntityNotFoundException("Request not found");

        if (request.getStatus() == null || request.getStatus().isEmpty())
            throw new IllegalOperationException("Status is invalid");

        request.setId(id);
        return adoptionRequestRepository.save(request);
    }

    @Transactional
    public void deleteAdoptionRequest(Long id) throws EntityNotFoundException {
        Optional<AdoptionRequestEntity> entity = adoptionRequestRepository.findById(id);
        if (entity.isEmpty())
            throw new EntityNotFoundException("Request not found");

        adoptionRequestRepository.deleteById(id);
    }
}