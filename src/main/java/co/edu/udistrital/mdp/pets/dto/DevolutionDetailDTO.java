package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DevolutionDetailDTO extends DevolutionDTO {

    private AdoptionDTO adoption;

    public DevolutionDetailDTO(DevolutionEntity entity) {
        super(entity);
        if (entity != null && entity.getAdoption() != null) {
            this.adoption = new AdoptionDTO(entity.getAdoption());
        }
    }
}
