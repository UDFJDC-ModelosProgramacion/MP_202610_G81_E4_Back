package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public AdoptionEntity createAdoption(AdoptionEntity adoption) {
        log.info("Iniciando creación de adopción");

        if (adoption.getPet() == null || adoption.getPet().getId() == null) {
            throw new IllegalArgumentException("El ID de la mascota es obligatorio");
        }
        if (adoption.getAdopter() == null || adoption.getAdopter().getId() == null) {
            throw new IllegalArgumentException("El ID del adoptante es obligatorio");
        }

        // Buscar y validar Mascota
        PetEntity pet = petRepository.findById(adoption.getPet().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));

        if (pet.getStatus() == null || !pet.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new IllegalStateException("La mascota no está disponible (Status actual: " + pet.getStatus() + ")");
        }

        // Buscar Adoptante
        AdopterEntity adopter = adopterRepository.findById(adoption.getAdopter().getId())
                .orElseThrow(() -> new EntityNotFoundException("Adoptante no encontrado"));

        // Vincular objetos reales
        adoption.setPet(pet);
        adoption.setAdopter(adopter);

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public List<AdoptionEntity> searchAdoptions() {
        return adoptionRepository.findAll();
    }

    @Transactional
    public AdoptionEntity searchAdoption(Long id) {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopción no encontrada"));
    }

    @Transactional
    public AdoptionEntity updateAdoption(Long id, AdoptionEntity adoption) {
        AdoptionEntity existing = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopción no encontrada"));

        if (adoption.getStatus() != null) existing.setStatus(adoption.getStatus());
        if (adoption.getAdoptionDate() != null) existing.setAdoptionDate(adoption.getAdoptionDate());

        return adoptionRepository.save(existing);
    }

    @Transactional
    public void deleteAdoption(Long id) {
        AdoptionEntity adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopción no encontrada"));

        if (!"FINISHED".equalsIgnoreCase(adoption.getStatus())) {
            throw new IllegalStateException("Solo se pueden eliminar adopciones con estado FINISHED");
        }

        adoptionRepository.delete(adoption);
    }
}

    

