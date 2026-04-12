package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeterinarianService {
    private static final String VET_NOT_FOUND = "Veterinarian not found with ID: ";

    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    @Lazy
    private VeterinarianService self;

    private static final List<String> VALID_SPECIALTIES = Arrays.asList(
            "General", "Surgery", "Dentistry", "Cardiology", "Dermatology"
    );

    @Transactional
    public VeterinarianEntity createVeterinarian(VeterinarianEntity vet) {
        log.info("Creando nuevo veterinario");
        if (vet.getSpecialties() != null) validateSpecialties(vet.getSpecialties());
        return veterinarianRepository.save(vet);
    }

    @Transactional(readOnly = true)
    public VeterinarianEntity searchVeterinarian(Long id) {
        log.info("Buscando veterinario con ID: {}", id);
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VET_NOT_FOUND + id));
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinarians() {
        log.info("Consultando todos los veterinarios");
        return veterinarianRepository.findAll().stream().toList();
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity vet) {
        log.info("Actualizando veterinario con ID: {}", id);
        VeterinarianEntity existing = self.searchVeterinarian(id);
        
        existing.setVeterinarianIdBusiness(vet.getVeterinarianIdBusiness());
        existing.setLastName(vet.getLastName());
        existing.setAvailability(vet.getAvailability());
        
        if (vet.getSpecialties() != null) {
            validateSpecialties(vet.getSpecialties());
            existing.setSpecialties(new ArrayList<>(vet.getSpecialties()));
        }

        if (vet.getShelter() != null && vet.getShelter().getId() != null) {
            existing.setShelter(vet.getShelter());
        }

        return veterinarianRepository.save(existing);
    }

    @Transactional
    public void deleteVeterinarian(Long id) {
        log.info("Eliminando veterinario con ID: {}", id);
        VeterinarianEntity vet = self.searchVeterinarian(id);
        veterinarianRepository.delete(vet);
    }

    private void validateSpecialties(List<String> specialties) {
        for (String specialty : specialties) {
            if (!VALID_SPECIALTIES.contains(specialty)) {
                log.error("Especialidad inválida detectada: {}", specialty);
                throw new IllegalArgumentException("Invalid specialty: " + specialty);
            }
        }
    }
}