package co.edu.udistrital.mdp.pets.dto;
import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrialStayDTO {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String observations;
    private String result; 
    private Long petId;
    private Long adoptionId; 

    public TrialStayDTO(TrialStayEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.observations = entity.getObservations();
            this.result = entity.getResult();
        }
        if (entity.getPet() != null) {
            this.petId = entity.getPet().getId();
        }
       if (entity.getAdoption() != null) {
                this.adoptionId = entity.getAdoption().getId();
       }
    }

    }  
