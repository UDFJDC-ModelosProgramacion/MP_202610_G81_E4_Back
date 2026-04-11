package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AdoptionHistoryDTO {

    private Long id;
    private LocalDate date;
    private String reason;
    private String detail;
    private Long adoptionId; 

    public AdoptionHistoryDTO(AdoptionHistoryEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.date = entity.getDate();
            this.reason = entity.getReason();
            this.detail = entity.getDetail();
            if (entity.getAdoption() != null) {
                this.adoptionId = entity.getAdoption().getId();
            }
        }
    }
}
