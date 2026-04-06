package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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

    @Transactional
    public ShelterEventEntity createShelterEvent(ShelterEventEntity event) {
        validateEvent(event);
        
        // Regla: Fecha futura
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        // Generación manual del código de negocio
        if (event.getEventCode() == null) {
            event.setEventCode((long) (new Random().nextInt(9000) + 1000));
        }

        return shelterEventRepository.save(event);
    }

    @Transactional(readOnly = true)
    public ShelterEventEntity searchShelterEventByCode(Long eventCode) {
        return shelterEventRepository.findByEventCode(eventCode.intValue())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with code: " + eventCode));
    }

    @Transactional(readOnly = true)
    public List<ShelterEventEntity> searchAllShelterEvents() {
        return shelterEventRepository.findAll();
    }

    @Transactional
    public ShelterEventEntity updateShelterEventByCode(Long eventCode, ShelterEventEntity event) {
        ShelterEventEntity existing = searchShelterEventByCode(eventCode);
        validateEvent(event);
        
        existing.setEventType(event.getEventType());
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setEventDate(event.getEventDate());
        existing.setLocation(event.getLocation());
        existing.setMaxCapacity(event.getMaxCapacity());
        existing.setRegisteredCount(event.getRegisteredCount());
        
        if (event.getShelter() != null) existing.setShelter(event.getShelter());

        return shelterEventRepository.save(existing);
    }

    @Transactional
    public void deleteShelterEventByCode(Long eventCode) {
        ShelterEventEntity event = searchShelterEventByCode(eventCode);
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot delete future events");
        }
        shelterEventRepository.delete(event);
    }

    private void validateEvent(ShelterEventEntity event) {
        if (event == null || event.getEventDate() == null || event.getShelter() == null) {
            throw new IllegalArgumentException("Invalid event data");
        }
    }
}