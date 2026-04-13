package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterService {
    private static final String SHELTER_NOT_FOUND = "Shelter not found";

    @Autowired
    private ShelterRepository shelterRepository;

    @Transactional
    public ShelterEntity createShelter(ShelterEntity shelter) {
        log.info("Creating shelter entity");
        validateShelter(shelter);
        return shelterRepository.save(shelter);
    }

    @Transactional(readOnly = true)
    public List<ShelterEntity> getShelters() {
        log.info("Searching all shelter entities");
        return shelterRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public ShelterEntity searchShelter(Long id) {
        log.info("Searching shelter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SHELTER_NOT_FOUND));
    }

    @Transactional
    public ShelterEntity updateShelter(Long id, ShelterEntity shelter) {
        log.info("Updating shelter with id: {}", id);
        if (id == null || shelter == null) {
            throw new IllegalArgumentException("Id and Shelter cannot be null");
        }

        ShelterEntity existing = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SHELTER_NOT_FOUND));

        existing.setName(shelter.getName());
        existing.setCity(shelter.getCity());
        existing.setAddress(shelter.getAddress());
        existing.setPhone(shelter.getPhone());
        existing.setEmail(shelter.getEmail());
        existing.setDescription(shelter.getDescription());

        return shelterRepository.save(existing);
    }

    @Transactional
    public void deleteShelter(Long id) {
        log.info("Deleting shelter with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        ShelterEntity shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(SHELTER_NOT_FOUND));

        if (shelter.getPets() != null && !shelter.getPets().isEmpty()) {
            throw new IllegalStateException("Cannot delete shelter with associated pets");
        }

        shelterRepository.delete(shelter);
        log.info("Shelter deleted successfully");
    }

    private void validateShelter(ShelterEntity shelter) {
        if (shelter == null) throw new IllegalArgumentException("Shelter cannot be null");
        if (shelter.getName() == null || shelter.getName().isEmpty()) throw new IllegalArgumentException("Name is required");
        if (shelter.getCity() == null || shelter.getCity().isEmpty()) throw new IllegalArgumentException("City is required");
        if (shelter.getAddress() == null || shelter.getAddress().isEmpty()) throw new IllegalArgumentException("Address is required");
        if (shelter.getPhone() == null || shelter.getPhone().isEmpty()) throw new IllegalArgumentException("Phone is required");
        if (shelter.getEmail() == null || shelter.getEmail().isEmpty()) throw new IllegalArgumentException("Email is required");
    }
}
