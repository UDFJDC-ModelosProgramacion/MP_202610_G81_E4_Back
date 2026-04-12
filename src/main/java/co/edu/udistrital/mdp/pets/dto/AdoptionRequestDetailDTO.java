package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionRequestDetailDTO extends AdoptionRequestDTO {

    private PetDTO pet;
    private AdopterDTO adopter;

    public AdoptionRequestDetailDTO(AdoptionRequestEntity entity) {
        super(entity); // Carga id, requestDate, status, motivation, etc.
        if (entity != null) {
            if (entity.getPet() != null) {
                this.pet = new PetDTO(entity.getPet());
            }
            if (entity.getAdopter() != null) {
                this.adopter = new AdopterDTO(entity.getAdopter());
            }
        }
    }
}