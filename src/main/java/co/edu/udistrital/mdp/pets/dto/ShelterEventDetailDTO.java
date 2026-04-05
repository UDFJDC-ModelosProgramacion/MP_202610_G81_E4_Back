package co.edu.udistrital.mdp.pets.dto;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShelterEventDetailDTO extends ShelterEventDTO {
    
    private ShelterDTO shelter;

    public ShelterEventDetailDTO (ShelterEventEntity entity) {
        super (entity);
        if (entity != null && entity.getShelter() != null) {
            this.shelter = new ShelterDTO(entity.getShelter());
        }
    }
}
