package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    
    private Long id;
    private String comments;
    private Integer rating; 
    private LocalDate reviewDate;
    private Long adoptionId;
    private Long adopterId;

    public ReviewDTO (ReviewEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.comments = entity.getComments();
            this.rating = entity.getRating();
            this.reviewDate = entity.getReviewDate();

            if (entity.getAdoption() != null) {
                this.adoptionId = entity.getAdoption().getId();
            }

            if (entity.getAdopter() != null) {
                this.adopterId = entity.getAdopter().getId();
            }
        }
    }
}
