package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReportEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ReportDTO {

    private Long id;
    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String data;
    private LocalDate generationDate;
    private Long shelterId;

    public ReportDTO(ReportEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.reportType = entity.getReportType();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.data = entity.getData();
            this.generationDate = entity.getGenerationDate();
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
            }
        }
    }
}
