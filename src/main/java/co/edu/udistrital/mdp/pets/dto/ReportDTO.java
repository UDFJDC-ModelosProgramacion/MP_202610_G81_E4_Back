package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import lombok.Data;

@Data
public class ReportDTO {
    private Long id;
    private String reportType;
    private Date startDate;
    private Date endDate;
    private String data;
    private Date generationDate;
}