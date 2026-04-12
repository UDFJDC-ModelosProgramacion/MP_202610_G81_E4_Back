package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdoptionTrackingDTO {

    private Long id;
    private String frequency;
    private String notes;
    private LocalDate nextReview;
    private Long adoptionId;

    public AdoptionTrackingDTO(AdoptionTrackingEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.frequency = entity.getFrequency();
            this.notes = entity.getNotes();
            this.nextReview = entity.getNextReview();
            if (entity.getAdoption() != null) {
                this.adoptionId = entity.getAdoption().getId();
            }
        }
    }
}
