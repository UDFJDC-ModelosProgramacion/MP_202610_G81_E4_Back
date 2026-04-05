package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import co.edu.udistrital.mdp.pets.entities.Veterinarian;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class VeterinarianService {
    
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    
    private static final List<String> VALID_SPECIALTIES = Arrays.asList(
        "General", "Surgery", "Dentistry", "Cardiology", "Dermatology"
    );
    

    public Veterinarian createVetterinarian(Veterinarian vet) {
        if (vet.getVeterinarianId() == null) {
            throw new IllegalArgumentException("Professional license is required");
        }
        
        if (vet.getSpecialties() != null) {
            for (String specialty : vet.getSpecialties()) {
                if (!VALID_SPECIALTIES.contains(specialty)) {
                    throw new IllegalArgumentException("Invalid specialty: " + specialty);
                }
            }
        }
        
        if (vet.getShelterId() == null) {
            throw new IllegalArgumentException("Veterinarian must be assigned to a shelter");
        }
        
        if (vet.getAvailability() == null || vet.getAvailability().isEmpty()) {
            throw new IllegalArgumentException("Availability schedule is required");
        }
        
        return veterinarianRepository.save(vet);
    }
    
    public Veterinarian updateVetterinarian(Long id, Veterinarian updatedVet) {
        Veterinarian existing = veterinarianRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinarian not found"));
        
        if (!existing.getVeterinarianId().equals(updatedVet.getVeterinarianId())) {
            throw new IllegalArgumentException("Cannot change veterinarian ID");
        }
        
        if (updatedVet.getSpecialties() != null) {
            for (String specialty : updatedVet.getSpecialties()) {
                if (!VALID_SPECIALTIES.contains(specialty)) {
                    throw new IllegalArgumentException("Invalid specialty: " + specialty);
                }
            }
            existing.setSpecialties(updatedVet.getSpecialties());
        }
        

        if (!shelterRepository.existsById(updatedVet.getShelterId())) {
             throw new IllegalArgumentException("Shelter does not exist");
        }
        
        existing.setAvailability(updatedVet.getAvailability());
        existing.setShelterId(updatedVet.getShelterId());
        
        return veterinarianRepository.save(existing);
    }
    
    public void deleteVetterinarian(Long id) {
        Veterinarian vet = veterinarianRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Veterinarian not found"));
        
 
        if (adoptionRepository.countByVeterinarian(id) > 0) {
             throw new IllegalStateException("Cannot delete vet with assigned adoptions");
        }
        

        if (medicalEventRepository.countPendingByVet(id) > 0) {
             throw new IllegalStateException("Cannot delete vet with pending medical events");
        }
        
        veterinarianRepository.deleteById(id);
    }
}