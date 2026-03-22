package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterService {
    @Autowired
    private ShelterRepository shelterRepository;
    public ShelterEntity createShelter(ShelterEntity shelter) {
        log.info("Creating shelter");
        if (shelter == null) {
            throw new IllegalArgumentException("Shelter cannot be null");
        }
        if (shelter.getName() == null || shelter.getName().isEmpty()) {
            throw new IllegalArgumentException("Shelter name is required");
        }
        if (shelter.getCity() == null || shelter.getCity().isEmpty()) {
            throw new IllegalArgumentException("Shelter city is required");
        }
        if (shelter.getAddress() == null || shelter.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Shelter address is required");
        }
        if (shelter.getPhone() == null || shelter.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Shelter phone is required");
        }
        if (shelter.getEmail() == null || shelter.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Shelter email is required");
        }
        ShelterEntity savedShelter = shelterRepository.save(shelter);
        log.info("Shelter created with id: {}", savedShelter.getId());
        return savedShelter;
    }
    public ShelterEntity searchShelter(Long id) {
        log.info("Searching shelter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
    }
    public ShelterEntity updateShelter(Long id, ShelterEntity shelter) {
        log.info("Updating shelter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (shelter == null) {
            throw new IllegalArgumentException("Shelter cannot be null");
        }
        ShelterEntity existing = shelterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
        existing.setName(shelter.getName());
        existing.setCity(shelter.getCity());
        existing.setAddress(shelter.getAddress());
        existing.setPhone(shelter.getPhone());
        existing.setEmail(shelter.getEmail());
        ShelterEntity updatedShelter = shelterRepository.save(existing);
        log.info("Shelter updated with id: {}", updatedShelter.getId());
        return updatedShelter;
    }
    public void deleteShelter(Long id) {
        log.info("Deleting shelter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ShelterEntity shelter = shelterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shelter not found"));
        if (shelter.getPets() != null && !shelter.getPets().isEmpty()) {
            throw new IllegalStateException("Cannot delete shelter with associated pets");
        }
        shelterRepository.delete(shelter);
    }
}
