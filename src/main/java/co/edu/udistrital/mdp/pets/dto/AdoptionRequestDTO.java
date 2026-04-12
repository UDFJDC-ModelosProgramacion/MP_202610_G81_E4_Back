package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdoptionRequestDTO {
    private Long id;
    private LocalDate requestDate;
    private String status;
    private String motivation;
    private Long petId;
    private Long adopterId; 

    public AdoptionRequestDTO(AdoptionRequestEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.requestDate = entity.getRequestDate();
            this.status = entity.getStatus();
            this.motivation = entity.getMotivation();
            if (entity.getPet() != null) this.petId = entity.getPet().getId();
            if (entity.getAdopter() != null) this.adopterId = entity.getAdopter().getId();
        }
    }
}