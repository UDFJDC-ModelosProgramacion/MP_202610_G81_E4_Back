package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrialStayDetailDTO extends TrialStayDTO {
    private AdoptionDTO adoption;

    public TrialStayDetailDTO(TrialStayEntity entity) {
        super(entity);

        if (entity != null) {
            if (entity.getAdoption() != null) {
                this.adoption = new AdoptionDTO(entity.getAdoption());
            }
        }
    }
}
