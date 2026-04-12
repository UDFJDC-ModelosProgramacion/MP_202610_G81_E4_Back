package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionTrackingDetailDTO extends AdoptionTrackingDTO {

    private AdoptionDTO adoption;

    public AdoptionTrackingDetailDTO(AdoptionTrackingEntity entity) {
        super(entity);
        if (entity != null && entity.getAdoption() != null) {
            this.adoption = new AdoptionDTO(entity.getAdoption());
        }
    }
}
