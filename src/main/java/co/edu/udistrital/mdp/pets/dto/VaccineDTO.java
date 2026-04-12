package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import co.edu.udistrital.mdp.pets.entities.VaccineEntity;

@Data
@NoArgsConstructor
public class VaccineDTO {

    private Long id;
    private String vaccineName;
    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private String batchNumber;
    private String observations;
    private Long vaccinationRecordId;

    public VaccineDTO(VaccineEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.vaccineName = entity.getVaccineName();
            this.applicationDate = entity.getApplicationDate();
            this.nextApplicationDate = entity.getNextApplicationDate();
            this.batchNumber = entity.getBatchNumber();
            this.observations = entity.getObservations();
            if (entity.getVaccinationRecord() != null) {
                this.vaccinationRecordId = entity.getVaccinationRecord().getId();
            }
        }
    }
}