package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionDTO {

    private Long id;
    private LocalDate adoptionDate;
    private String status;
    private Long petId;
    private Long adopterId;
    private Long trialStayId;
    private Long reviewId;
    private Long veterinarianId;

    public AdoptionDTO(AdoptionEntity entity) {
        if (entity == null) return;

        this.id = entity.getId();
        this.adoptionDate = entity.getAdoptionDate();
        this.status = entity.getStatus();
        
        this.petId = (entity.getPet() != null) ? entity.getPet().getId() : null;
        this.adopterId = (entity.getAdopter() != null) ? entity.getAdopter().getId() : null;
        this.trialStayId = (entity.getTrialStay() != null) ? entity.getTrialStay().getId() : null;
        this.reviewId = (entity.getReview() != null) ? entity.getReview().getId() : null;
        this.veterinarianId = (entity.getVeterinarian() != null) ? entity.getVeterinarian().getId() : null;
    }

    public AdoptionEntity toEntity() {
        AdoptionEntity entity = new AdoptionEntity();
        entity.setId(this.id);
        entity.setAdoptionDate(this.adoptionDate);
        entity.setStatus(this.status);
        return entity;
    }
}