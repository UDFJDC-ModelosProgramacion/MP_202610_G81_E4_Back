package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class MedicalEventDTO {

    private Long id;

    private String eventType;
    private LocalDate eventDate;
    private String description;
    private String diagnosis;
    private String treatment;

    private List<String> attachments;

}