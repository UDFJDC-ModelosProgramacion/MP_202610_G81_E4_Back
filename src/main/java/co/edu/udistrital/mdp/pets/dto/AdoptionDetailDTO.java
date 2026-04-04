package co.edu.udistrital.mdp.pets.dto;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionDetailDTO extends AdoptionDTO {
    private ReviewDTO review;
    private TrialStayDTO trialStay;

    public AdoptionDetailDTO(AdoptionEntity entity) {
        super(entity);
        if (entity.getReview() != null) {
                this.review = new ReviewDTO(entity.getReview());
        }
        if (entity.getTrialStay() != null) {
                this.trialStay = new TrialStayDTO(entity.getTrialStay());
        }
        
    }
}
