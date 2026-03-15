package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterEventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterEventService {
    @Autowired
    private ShelterEventRepository shelterEventRepository;
    public ShelterEventEntity createShelterEvent(ShelterEventEntity event) {
        log.info("Creating shelter event");
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        if (event.getEventDate() == null) {
            throw new IllegalArgumentException("Event date cannot be null");
        } 
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        if (event.getShelter() == null) {
            throw new IllegalArgumentException("Shelter cannot be null");
        }
        return shelterEventRepository.save(event);
    }
    public ShelterEventEntity searchShelterEvent(Long id) {
        log.info("Searching shelter event with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return shelterEventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter event not found"));
    }
    public List<ShelterEventEntity> searchShelterEvents() {
        log.info("Searching all shelter events");

        return shelterEventRepository.findAll();
    }
    public ShelterEventEntity updateShelterEvent(Long id, ShelterEventEntity event) {
        log.info("Updating shelter event with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event cannot be null");
        }
        ShelterEventEntity existing = shelterEventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shelter event not found"));
        existing.setEventCode(event.getEventCode());
        existing.setEventType(event.getEventType());
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        existing.setLocation(event.getLocation());
        existing.setMaxCapacity(event.getMaxCapacity());
        existing.setRegisteredCount(event.getRegisteredCount());
        return shelterEventRepository.save(existing);
    }
    public void deleteShelterEvent(Long id) {
        log.info("Deleting shelter event with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ShelterEventEntity event = shelterEventRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shelter event not found"));
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot delete an event that has not occurred yet");
        }
        shelterEventRepository.delete(event);
    }
}
