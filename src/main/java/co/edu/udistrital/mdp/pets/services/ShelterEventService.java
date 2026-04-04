package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterEventRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterEventService {

    @Autowired
    private ShelterEventRepository shelterEventRepository;

    @Transactional
    public ShelterEventEntity createShelterEvent(ShelterEventEntity event) {
        log.info("Creating shelter event");
        validateEvent(event);
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }
        if (event.getEventCode() == null) {
            event.setEventCode(new Random().nextInt(9000) + 1000);
        }
        return shelterEventRepository.save(event);
    }

    public ShelterEventEntity searchShelterEventByCode(Integer eventCode) {
        log.info("Searching shelter event with code: {}", eventCode);
        if (eventCode == null) {
            throw new IllegalArgumentException("Event code cannot be null");
        }
        return shelterEventRepository.findByEventCode(eventCode)
                .orElseThrow(() -> new EntityNotFoundException("Shelter event with code " + eventCode + " not found"));
    }

    public List<ShelterEventEntity> searchAllShelterEvents() {
        log.info("Searching all shelter events");
        return shelterEventRepository.findAll();
    }

    @Transactional
    public ShelterEventEntity updateShelterEventByCode(Integer eventCode, ShelterEventEntity event) {
        log.info("Updating shelter event with code: {}", eventCode);
        validateEvent(event);
        ShelterEventEntity existing = searchShelterEventByCode(eventCode);

        existing.setEventType(event.getEventType());
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        existing.setLocation(event.getLocation());
        existing.setMaxCapacity(event.getMaxCapacity());
        existing.setRegisteredCount(event.getRegisteredCount());
        
        if (event.getShelter() != null) {
            existing.setShelter(event.getShelter());
        }

        return shelterEventRepository.save(existing);
    }

    @Transactional
    public void deleteShelterEventByCode(Integer eventCode) {
        log.info("Deleting shelter event with code: {}", eventCode);
        ShelterEventEntity event = searchShelterEventByCode(eventCode);

        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot delete an event that has not occurred yet");
        }
        
        shelterEventRepository.delete(event);
    }

    private void validateEvent(ShelterEventEntity event) {
        if (event == null) throw new IllegalArgumentException("Event cannot be null");
        if (event.getEventDate() == null) throw new IllegalArgumentException("Event date cannot be null");
        if (event.getShelter() == null) throw new IllegalArgumentException("Shelter cannot be null");
    }
}