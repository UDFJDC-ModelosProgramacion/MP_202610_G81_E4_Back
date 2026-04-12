package co.edu.udistrital.mdp.pets.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.Adopter;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    // ==================== CREATE ====================

    public Adopter createAdopter(Adopter adopter) {

        if (adopter.getAddress() == null || adopter.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        String[] validHousingTypes = {"Casa", "Apartamento", "Finca"};
        if (!Arrays.asList(validHousingTypes).contains(adopter.getHousingType())) {
            throw new IllegalArgumentException("Invalid housing type");
        }

        if (adopterRepository.existsByEmail(adopter.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return adopterRepository.save(adopter);
    }

    // ==================== UPDATE ====================

    public Adopter updateAdopter(Long id, Adopter updatedAdopter) {

        Adopter existing = adopterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adopter not found"));

        if (!existing.getAdopterId().equals(updatedAdopter.getAdopterId())) {
            throw new IllegalArgumentException("Cannot change adopterId");
        }

        if (updatedAdopter.getHasChildren() == null) {
            updatedAdopter.setHasChildren(false);
        }

        if (updatedAdopter.getHasOtherPets() == null) {
            updatedAdopter.setHasOtherPets(false);
        }

        existing.setAddress(updatedAdopter.getAddress());
        existing.setHousingType(updatedAdopter.getHousingType());
        existing.setHasChildren(updatedAdopter.getHasChildren());
        existing.setHasOtherPets(updatedAdopter.getHasOtherPets());

        return adopterRepository.save(existing);
    }

    // ==================== DELETE ====================

    public void deleteAdopter(Long id) {

        Adopter adopter = adopterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adopter not found"));

        // ⚠️ Estas validaciones dependen de otros repositorios que aún no tienes
        // 👉 Déjalas comentadas para que compile

        /*
        if (adoptionRequestRepository.countPendingByAdopter(id) > 0) {
            throw new IllegalStateException("Cannot delete adopter with pending requests");
        }

        if (adoptionRepository.countActiveByAdopter(id) > 0) {
            throw new IllegalStateException("Cannot delete adopter with active adoptions");
        }
        */

        adopterRepository.deleteById(id);
    }

    // ==================== FIND ALL ====================

    public List<Adopter> findAll() {
        return adopterRepository.findAll();
    }

    // ==================== FIND BY ID ====================

    public Optional<Adopter> findById(Long id) {
        return adopterRepository.findById(id);
    }
}