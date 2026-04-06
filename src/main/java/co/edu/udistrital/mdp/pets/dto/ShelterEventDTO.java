package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterEventDTO {
    
    private Long id;
    private Long eventCode;
    private String eventType;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private int maxCapacity;
    private int registeredCount;
    private ShelterDTO shelter;

    public ShelterEventDTO (ShelterEventEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.eventCode = entity.getEventCode();
            this.eventType = entity.getEventType();
            this.title = entity.getTitle();
            this.description = entity.getDescription();
            this.eventDate = entity.getEventDate(); 
            this.location = entity.getLocation();
            this.maxCapacity = entity.getMaxCapacity();
            this.registeredCount = entity.getRegisteredCount();

            if (entity.getShelter() != null) {
                this.shelter = new ShelterDTO(entity.getShelter());
            }   
        }
    }
}
