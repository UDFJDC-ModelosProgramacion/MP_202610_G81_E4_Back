package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import lombok.Data;

@Data
public class DevolutionDTO {
    private Long id;
    private String reason;
    private String detailedDescription;
    private String petState;
    private Date returnDate;
}