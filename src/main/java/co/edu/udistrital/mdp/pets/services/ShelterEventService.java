package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterEventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShelterEventService {

    @Autowired
    private ShelterEventRepository shelterEventRepository;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String EVENT_NOT_FOUND = "Event not found with code: ";

    @Transactional
    public ShelterEventEntity createShelterEvent(ShelterEventEntity event) {
        if (event.getEventDate() != null && event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        if (event.getEventCode() == null) {
            event.setEventCode((long) (SECURE_RANDOM.nextInt(900000) + 100000));
        }

        log.info("Creando evento con código: {}", event.getEventCode());
        return shelterEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public ShelterEventEntity searchShelterEventByCode(Long eventCode) {
        return shelterEventRepository.findByEventCode(eventCode)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND + eventCode));
    }

    @Transactional(readOnly = true)
    public List<ShelterEventEntity> searchAllShelterEvents() {
        log.info("Fetching all shelter events");
        return shelterEventRepository.findAll().stream().toList();
    }

    @Transactional
    public ShelterEventEntity updateShelterEventByCode(Long eventCode, ShelterEventEntity eventData) {
        log.info("Updating event with code: {}", eventCode);
        ShelterEventEntity existing = shelterEventRepository.findByEventCode(eventCode)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND + eventCode));
        
        existing.setEventType(eventData.getEventType());
        existing.setTitle(eventData.getTitle());
        existing.setDescription(eventData.getDescription());
        existing.setEventDate(eventData.getEventDate());
        existing.setLocation(eventData.getLocation());
        existing.setMaxCapacity(eventData.getMaxCapacity());
        existing.setRegisteredCount(eventData.getRegisteredCount());
        
        if (eventData.getShelter() != null) {
            existing.setShelter(eventData.getShelter());
        }

        return shelterEventRepository.save(existing);
    }

    @Transactional
    public void deleteShelterEventByCode(Long eventCode) {
        ShelterEventEntity event = shelterEventRepository.findByEventCode(eventCode)
                .orElseThrow(() -> new EntityNotFoundException(EVENT_NOT_FOUND + eventCode));
                
        if (event.getEventDate() != null && event.getEventDate().isAfter(LocalDateTime.now())) {
            log.error("Intento fallido de eliminar evento futuro con código: {}", eventCode);
            throw new IllegalStateException("Cannot delete a future event");
        }

        shelterEventRepository.delete(event);
        log.info("Evento con código {} eliminado con éxito", eventCode);
    }
}