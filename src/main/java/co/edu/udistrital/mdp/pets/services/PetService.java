package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private ShelterRepository shelterRepository;

    @Transactional
    public PetEntity createPet(PetEntity pet) throws IllegalOperationException{
        log.info("inicia proceso de creacion de mascota") ;
        if (pet.getName() == null || pet.getName().isEmpty())
            throw new IllegalOperationException("Pet name is not valid");
        
        if (pet.getSpecies() == null || pet.getSpecies().isEmpty())
            throw new IllegalOperationException("pet species is not valid");
        if (pet.getStatus() == null || pet.getStatus().isEmpty())
            throw new IllegalOperationException("pet status is not valid");
        if (pet.getShelter() == null)
            throw new IllegalOperationException("Shelter is not valid");
        Optional<ShelterEntity> shelterEntity = shelterRepository.findById(pet.getShelter().getId());
        if (shelterEntity.isEmpty())
            throw new IllegalOperationException("Shelter does not exist");
        pet.setShelter(shelterEntity.get());
        PetEntity newPet = petRepository.save(pet);
        log.info("Termina proceso de creacion de mascota");
        return newPet;
    }

    @Transactional
    public List<PetEntity> getPets() {
        log.info("consulta todas las mascotas");
        return petRepository.findAll();
    }
    @Transactional
    public PetEntity getPet(Long petId) throws EntityNotFoundException { 
        log.info("Consulta mascota con id = {}", petId);
        Optional<PetEntity> petEntity = petRepository.findById(petId);
        if (petEntity.isEmpty()){
            throw new EntityNotFoundException(("pet not found"));
        }
        return petEntity.get();
    }
    @Transactional
    public PetEntity updatePet(Long petId, PetEntity pet)
        throws EntityNotFoundException, IllegalOperationException{
        log.info("Inicia actualizacion de mascota");
        Optional<PetEntity> existingPet = petRepository.findById(petId);
        if(existingPet.isEmpty()){
            throw new EntityNotFoundException(("Pet not found"));
        }
        if(pet.getName() == null || pet.getName().isEmpty()){
            throw new IllegalOperationException("Pet name is not valid");
        }
        pet.setId(petId);
        
        return petRepository.save(pet);
    }
    @Transactional
    public void deletePet(Long petId)
        throws EntityNotFoundException, IllegalOperationException{
        log.info("Inicia eliminacion de mascota");
        Optional<PetEntity> petEntity = petRepository.findById(petId);

        if(petEntity.isEmpty())
            throw new EntityNotFoundException("Pet not found");
        if (!petEntity.get().getVaccinationRecords().isEmpty())
            throw new IllegalOperationException("Cannot delete pet with vaccination records");
        petRepository.deleteById(petId);
        log.info("Se eliminó la mascota correctamente");
            
        
    }
}
