package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdoptionDTO {

    private Long id;
    private LocalDate adoptionDate;
    private String status;
    private Long petId;
    private Long veterinarianId;

    public AdoptionDTO(AdoptionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.adoptionDate = entity.getAdoptionDate();
            this.status = entity.getStatus();
            
            if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
            }
            if (entity.getVeterinarian() != null) {
                this.veterinarianId = entity.getVeterinarian().getId();
            }
        }
    }
}