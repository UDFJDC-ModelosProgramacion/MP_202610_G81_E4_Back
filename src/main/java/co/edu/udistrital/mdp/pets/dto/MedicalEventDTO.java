package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class MedicalEventDTO {

    private Long id;
    private String eventType;
    private LocalDate eventDate;
    private String description;
    private String diagnosis;
    private String treatment;
    private Long petId;
    private List<String> attachments;

    public MedicalEventDTO(MedicalEventEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.eventType = entity.getEventType();
            this.eventDate = entity.getEventDate();
            this.description = entity.getDescription();
            this.diagnosis = entity.getDiagnosis();
            this.treatment = entity.getTreatment();
            
            if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
            }
        
            this.attachments = entity.getAttachments();
        }
    }
}