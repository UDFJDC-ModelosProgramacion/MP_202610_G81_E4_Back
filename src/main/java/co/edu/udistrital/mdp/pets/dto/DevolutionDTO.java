package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DevolutionDTO {
    private Long id;
    private String reason;
    private String detailedDescription;
    private String petState;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date returnDate;
}