package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.repositories.*;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AdoptionService {
    private static final String ADOPTION_NOT_FOUND = "Adopción no encontrada";

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public AdoptionEntity createAdoption(AdoptionEntity adoption) 
            throws IllegalOperationException, EntityNotFoundException {
        log.info("Iniciando creación de adopción");

        if (adoption.getPet() == null || adoption.getPet().getId() == null) {
            throw new IllegalOperationException("El ID de la mascota es obligatorio");
        }
        if (adoption.getAdopter() == null || adoption.getAdopter().getId() == null) {
            throw new IllegalOperationException("El ID del adoptante es obligatorio");
        }

        PetEntity pet = petRepository.findById(adoption.getPet().getId())
                .orElseThrow(() -> new EntityNotFoundException("Mascota no encontrada"));

        AdopterEntity adopter = adopterRepository.findById(adoption.getAdopter().getId())
                .orElseThrow(() -> new EntityNotFoundException("Adoptante no encontrado"));

        if (pet.getStatus() == null || !pet.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new IllegalOperationException("La mascota con ID " + pet.getId() + " no está disponible para adopción");
        }
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        
        pet.setStatus("ADOPTED");
        petRepository.save(pet);

        AdoptionEntity newAdoption = adoptionRepository.save(adoption);
        log.info("Adopción creada exitosamente con ID: {}", newAdoption.getId());
        
        return newAdoption;
    }
    @Transactional(readOnly = true)
    public List<AdoptionEntity> searchAdoptions() {
        return adoptionRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public AdoptionEntity searchAdoption(Long id) throws EntityNotFoundException {
        return adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADOPTION_NOT_FOUND));
    }

    @Transactional
    public AdoptionEntity updateAdoption(Long id, AdoptionEntity adoptionData) 
            throws EntityNotFoundException, IllegalOperationException { 
    
        AdoptionEntity existing = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADOPTION_NOT_FOUND));

        if ("FINISHED".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalOperationException("No se puede modificar una adopción con estado FINISHED");
        }

        if (adoptionData.getStatus() != null) {
            existing.setStatus(adoptionData.getStatus());
        }
        
        if (adoptionData.getAdoptionDate() != null) {
            existing.setAdoptionDate(adoptionData.getAdoptionDate());
        }

        return adoptionRepository.save(existing);
    }

   @Transactional
    public void deleteAdoption(Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Intentando eliminar adopción con ID: {}", id);

        AdoptionEntity adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADOPTION_NOT_FOUND));

        if (!"FINISHED".equalsIgnoreCase(adoption.getStatus())) {
            log.warn("Intento fallido de eliminar adopción no finalizada: {}", id);
            throw new IllegalOperationException("Solo se pueden eliminar adopciones con estado FINISHED para preservar el historial activo.");
        }
        adoptionRepository.delete(adoption);
        log.info("Adopción ID {} eliminada correctamente", id);
    }
}

    

