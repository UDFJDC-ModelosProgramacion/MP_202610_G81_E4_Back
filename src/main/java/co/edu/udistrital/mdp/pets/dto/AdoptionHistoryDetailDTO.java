package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionHistoryDetailDTO extends AdoptionHistoryDTO {

    private AdoptionDTO adoption;

    public AdoptionHistoryDetailDTO(AdoptionHistoryEntity entity) {
        super(entity);
        if (entity != null && entity.getAdoption() != null) {
            this.adoption = new AdoptionDTO(entity.getAdoption());
        }
    }
}
