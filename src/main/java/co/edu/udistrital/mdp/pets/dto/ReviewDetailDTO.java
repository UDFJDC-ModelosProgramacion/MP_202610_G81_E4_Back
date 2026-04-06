package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReviewDetailDTO extends ReviewDTO {
    
    private AdoptionDTO adoption;
    
    public ReviewDetailDTO(ReviewEntity entity) {
        super(entity);
        if (entity.getAdoption() != null) {
                this.adoption = new AdoptionDTO(entity.getAdoption());
        }
    }
    
}
