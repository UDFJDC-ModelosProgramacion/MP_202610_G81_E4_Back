package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Transactional(readOnly = true)
    public List<PetEntity> searchPets() {
        log.info("Consultando todas las mascotas");
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public PetEntity searchPet(Long id) {
        log.info("Buscando mascota con ID: {}", id);
        return petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la mascota con ID: " + id));
    }

    @Transactional
    public PetEntity createPet(PetEntity pet) {
        log.info("Guardando nueva mascota: {}", pet.getName());
        return petRepository.save(pet);
    }

    @Transactional
    public PetEntity updatePet(Long id, PetEntity petData) {
        PetEntity existing = searchPet(id);
        
        existing.setName(petData.getName());
        existing.setSpecies(petData.getSpecies());
        existing.setBreed(petData.getBreed());
        existing.setAge(petData.getAge());
        existing.setStatus(petData.getStatus());
        
        if (petData.getShelter() != null) {
            existing.setShelter(petData.getShelter());
        }

        return petRepository.save(existing);
    }

    @Transactional
    public void deletePet(Long id) {
        PetEntity pet = searchPet(id);
        petRepository.delete(pet);
        log.info("Mascota con ID: {} eliminada", id);
    }
}
