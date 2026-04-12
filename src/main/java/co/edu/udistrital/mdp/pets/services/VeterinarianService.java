package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Arrays;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;

@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    private static final List<String> VALID_SPECIALTIES = Arrays.asList(
            "General", "Surgery", "Dentistry", "Cardiology", "Dermatology"
    );

    @Transactional
    public VeterinarianEntity createVeterinarian(VeterinarianEntity vet) {
        if (vet.getSpecialties() != null) validateSpecialties(vet.getSpecialties());
        return veterinarianRepository.save(vet);
    }

    @Transactional(readOnly = true)
    public VeterinarianEntity searchVeterinarian(Long id) {
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinarians() {
        return veterinarianRepository.findAll();
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity vet) {
        VeterinarianEntity existing = veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with ID: " + id));
        existing.setVeterinarianIdBusiness(vet.getVeterinarianIdBusiness());
        existing.setLastName(vet.getLastName());
        existing.setAvailability(vet.getAvailability());
        
        if (vet.getSpecialties() != null) {
            validateSpecialties(vet.getSpecialties());
            existing.setSpecialties(vet.getSpecialties());
        }

        if (vet.getShelter() != null && vet.getShelter().getId() != null) {
            existing.setShelter(vet.getShelter());
        }

        return veterinarianRepository.save(existing);
    }

    @Transactional
    public void deleteVeterinarian(Long id) {
        VeterinarianEntity vet = searchVeterinarian(id);
        veterinarianRepository.delete(vet);
    }

    private void validateSpecialties(List<String> specialties) {
        for (String specialty : specialties) {
            if (!VALID_SPECIALTIES.contains(specialty)) {
                throw new IllegalArgumentException("Invalid specialty: " + specialty);
            }
        }
    }
}