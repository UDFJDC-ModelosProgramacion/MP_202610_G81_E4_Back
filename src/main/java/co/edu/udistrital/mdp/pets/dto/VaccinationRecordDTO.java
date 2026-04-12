package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VaccinationRecordDTO {

    private Long id;
    private Long petId;

    public VaccinationRecordDTO(VaccinationRecordEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
            }
        }
    }
}