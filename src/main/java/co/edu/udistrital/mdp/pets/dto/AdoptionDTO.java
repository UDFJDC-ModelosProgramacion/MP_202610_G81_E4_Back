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
        if (entity != null) {
            this.id = entity.getId();
            this.adoptionDate = entity.getAdoptionDate();
            this.status = entity.getStatus();

         if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
        }
            
        if (entity.getAdopter() != null) {
                this.adopterId = entity.getAdopter().getId();
        }

        if (entity.getTrialStay() != null) {
                this.trialStayId = entity.getTrialStay().getId();
        }

        if (entity.getReview() != null) {
                this.reviewId = entity.getReview().getId();
        }

        if (entity.getVeterinarian() != null) {
                this.veterinarianId = entity.getVeterinarian().getId();
            }
        }
    }
}