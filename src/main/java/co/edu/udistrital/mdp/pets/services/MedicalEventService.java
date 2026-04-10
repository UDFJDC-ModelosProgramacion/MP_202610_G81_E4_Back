package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicalEventService {

    @Autowired
    private MedicalEventRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public MedicalEventEntity createMedicalEvent(MedicalEventEntity event)
            throws IllegalOperationException {

        if (event.getPet() == null)
            throw new IllegalOperationException("Pet is required");

        if (event.getEventType() == null || event.getEventType().isEmpty())
            throw new IllegalOperationException("Type is invalid");

        Optional<PetEntity> pet = petRepository.findById(event.getPet().getId());
        if (pet.isEmpty())
            throw new IllegalOperationException("Pet does not exist");

        event.setPet(pet.get());
        return repository.save(event);
    }

    @Transactional
    public List<MedicalEventEntity> getMedicalEvents() {
        return repository.findAll();
    }

    @Transactional
    public MedicalEventEntity getMedicalEvent(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Not found"));
    }

    @Transactional
    public void deleteMedicalEvent(Long id) throws EntityNotFoundException {
        if (repository.findById(id).isEmpty())
            throw new EntityNotFoundException("Not found");

        repository.deleteById(id);
    }
}