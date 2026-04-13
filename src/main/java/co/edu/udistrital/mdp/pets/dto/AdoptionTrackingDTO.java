package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AdoptionTrackingDTO {
    private Long id;
    private String frequency;
    private String notes;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date nextReview;
}