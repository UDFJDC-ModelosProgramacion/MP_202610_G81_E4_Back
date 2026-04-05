package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Arrays;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private static final List<String> VALID_SPECIALTIES = Arrays.asList(
            "General", "Surgery", "Dentistry", "Cardiology", "Dermatology"
    );

    @Transactional
    public VeterinarianEntity createVeterinarian(VeterinarianEntity vet) {
        log.info("Creating Veterinarian");
        
        if (vet == null) {
            throw new IllegalArgumentException("Veterinarian cannot be null");
        }
        
        if (vet.getVeterinarianIdBusiness() == null) {
            throw new IllegalArgumentException("Professional license (business id) is required");
        }

        if (vet.getSpecialties() != null) {
            for (String specialty : vet.getSpecialties()) {
                if (!VALID_SPECIALTIES.contains(specialty)) {
                    throw new IllegalArgumentException("Invalid specialty: " + specialty);
                }
            }
        }

        if (vet.getShelter() == null) {
            throw new IllegalArgumentException("Veterinarian must be assigned to a shelter");
        }

        VeterinarianEntity savedVet = veterinarianRepository.save(vet);
        log.info("Veterinarian created with id: {}", savedVet.getId());
        return savedVet;
    }

    @Transactional(readOnly = true)
    public VeterinarianEntity searchVeterinarian(Long id) {
        log.info("Searching Veterinarian with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found"));
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinarians() {
        log.info("Searching all veterinarians");
        return veterinarianRepository.findAll();
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity vet) {
        log.info("Updating Veterinarian with id: {}", id);
        
        if (id == null || vet == null) {
            throw new IllegalArgumentException("Id and Veterinarian object cannot be null");
        }

        VeterinarianEntity existing = veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found"));

        if (vet.getVeterinarianIdBusiness() != null && 
            !existing.getVeterinarianIdBusiness().equals(vet.getVeterinarianIdBusiness())) {
            throw new IllegalArgumentException("Cannot change veterinarian business ID");
        }

        existing.setLastName(vet.getLastName());
        existing.setSpecialties(vet.getSpecialties());
        existing.setAvailability(vet.getAvailability());
        existing.setShelter(vet.getShelter());

        VeterinarianEntity updatedVet = veterinarianRepository.save(existing);
        log.info("Veterinarian updated with id: {}", updatedVet.getId());
        return updatedVet;
    }

    @Transactional
    public void deleteVeterinarian(Long id) {
        log.info("Deleting Veterinarian with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        VeterinarianEntity vet = veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found"));

        if (vet.getAdoptions() != null && !vet.getAdoptions().isEmpty()) {
            throw new IllegalStateException("Cannot delete vet with assigned adoptions");
        }

        if (vet.getMedicalEvents() != null && !vet.getMedicalEvents().isEmpty()) {
            throw new IllegalStateException("Cannot delete vet with medical events");
        }

        veterinarianRepository.delete(vet);
        log.info("Veterinarian deleted successfully");
    }
}