package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {
    private static final String PET_NOT_FOUND = "Pet not found";

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Transactional
    public PetEntity createPet(PetEntity pet) throws IllegalOperationException {
        log.info("Iniciando creación de mascota...");

        if (pet.getName() == null || pet.getName().isEmpty())
            throw new IllegalOperationException("Pet name is not valid");

        if (pet.getSpecies() == null || pet.getSpecies().isEmpty())
            throw new IllegalOperationException("Pet species is not valid");

        if (pet.getStatus() == null || pet.getStatus().isEmpty()) {
            pet.setStatus("AVAILABLE");
        }

        if (pet.getShelter() == null || pet.getShelter().getId() == null)
            throw new IllegalOperationException("Shelter ID is required");
            
        ShelterEntity shelterEntity = shelterRepository.findById(pet.getShelter().getId())
                .orElseThrow(() -> new IllegalOperationException("Shelter does not exist"));

        pet.setShelter(shelterEntity);
        
        PetEntity newPet = petRepository.save(pet);
        
        log.info("Pet created with ID: {}", newPet.getId());
        return newPet;
    }

    @Transactional(readOnly = true)
    public List<PetEntity> getPets() {
        log.info("Searching all pets...");
        return petRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public PetEntity getPet(Long petId) throws EntityNotFoundException {
        log.info("Searching pet with ID: {}", petId);
        return petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(PET_NOT_FOUND));
    }

    @Transactional
    public PetEntity updatePet(Long petId, PetEntity petData) 
            throws EntityNotFoundException, IllegalOperationException {
        
        PetEntity existing = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(PET_NOT_FOUND));

        log.info("Updating pet with ID: {}", petId);

        if (petData.getName() != null && !petData.getName().isEmpty()) {
            existing.setName(petData.getName());
        }
        
        existing.setSpecies(petData.getSpecies());
        existing.setBreed(petData.getBreed());
        existing.setAge(petData.getAge());
        existing.setStatus(petData.getStatus());
        
        if (petData.getShelter() != null && petData.getShelter().getId() != null) {
            ShelterEntity shelter = shelterRepository.findById(petData.getShelter().getId())
                    .orElseThrow(() -> new IllegalOperationException("Shelter does not exist"));
            existing.setShelter(shelter);
        }

        return petRepository.save(existing);
    }

    @Transactional
    public void deletePet(Long petId) throws EntityNotFoundException, IllegalOperationException {
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(PET_NOT_FOUND));

        log.info("Attempting to delete pet with ID: {}", petId);

        if (pet.getVaccinationRecords() != null && !pet.getVaccinationRecords().isEmpty()) {
            throw new IllegalOperationException("Cannot delete pet with vaccination records");
        }

        petRepository.delete(pet);
        log.info("Pet with ID: {} deleted successfully", petId);
    }
}
