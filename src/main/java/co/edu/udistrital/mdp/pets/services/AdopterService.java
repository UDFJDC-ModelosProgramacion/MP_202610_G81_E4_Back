package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Arrays;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private AdoptionRepository adoptionRepository;

    public AdopterEntity createAdopter(AdopterEntity adopter) {
        log.info("Creating Adopter");
        if (adopter == null) {
            throw new IllegalArgumentException("Adopter cannot be null");
        }
        if (adopter.getAdopterIdBusiness() == null) {
            throw new IllegalArgumentException("Adopter business ID is required");
        }
        
        // Validación de tipo de vivienda (Lógica de negocio)
        List<String> validHousing = Arrays.asList("Casa", "Apartamento", "Finca");
        if (adopter.getHousingType() == null || !validHousing.contains(adopter.getHousingType())) {
            throw new IllegalArgumentException("Invalid housing type");
        }

        AdopterEntity savedAdopter = adopterRepository.save(adopter);
        log.info("Adopter created with id: {}", savedAdopter.getId());
        return savedAdopter;
    }

    public AdopterEntity searchAdopter(Long id) {
        log.info("Searching Adopter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"));
    }

    public List<AdopterEntity> searchAdopters() {
        log.info("Searching all adopters");
        return adopterRepository.findAll();
    }

    public AdopterEntity updateAdopter(Long id, AdopterEntity adopter) {
        log.info("Updating Adopter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (adopter == null) {
            throw new IllegalArgumentException("Adopter cannot be null");
        }

        AdopterEntity existing = adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"));

        // Impedir cambio de identificación de negocio
        if (adopter.getAdopterIdBusiness() != null && !existing.getAdopterIdBusiness().equals(adopter.getAdopterIdBusiness())) {
            throw new IllegalArgumentException("Cannot change adopter business ID");
        }

        existing.setLastName(adopter.getLastName());
        existing.setAddress(adopter.getAddress());
        existing.setHousingType(adopter.getHousingType());
        existing.setHasChildren(adopter.getHasChildren());
        existing.setHasOtherPets(adopter.getHasOtherPets());
        existing.setPreferences(adopter.getPreferences());

        AdopterEntity updatedAdopter = adopterRepository.save(existing);
        log.info("Adopter updated with id: {}", updatedAdopter.getId());
        return updatedAdopter;
    }

    public void deleteAdopter(Long id) {
        log.info("Deleting Adopter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        AdopterEntity adopter = adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Adopter not found"));

       
        if (adoptionRequestRepository.findAll().stream()
            .anyMatch(request -> request.getAdopter() != null && id.equals(request.getAdopter().getId()))) {
            throw new IllegalStateException("Adopter has associated adoption requests");
        }

        if (adoptionRepository.findAll().stream()
            .anyMatch(adoption -> adoption.getAdopter() != null && id.equals(adoption.getAdopter().getId()))) {
            throw new IllegalStateException("Adopter has associated adoption records");
        }

        adopterRepository.delete(adopter);
        log.info("Adopter deleted successfully");
    }
}