package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DevolutionDTO {

    private Long id;
    private LocalDate returnDate;
    private String reason;
    private String detailedDescription;
    private String petState;
    private Long adoptionId;

    public DevolutionDTO(DevolutionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.returnDate = entity.getReturnDate();
            this.reason = entity.getReason();
            this.detailedDescription = entity.getDetailedDescription();
            this.petState = entity.getPetState();
            if (entity.getAdoption() != null) {
                this.adoptionId = entity.getAdoption().getId();
            }
        }
    }
}