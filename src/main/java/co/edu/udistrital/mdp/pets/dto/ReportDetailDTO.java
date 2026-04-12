package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReportDetailDTO extends ReportDTO {

    private ShelterDTO shelter; 
    public ReportDetailDTO(ReportEntity entity) {
        super(entity);
        if (entity != null && entity.getShelter() != null) {
            this.shelter = new ShelterDTO(entity.getShelter());
        }
    }
}
