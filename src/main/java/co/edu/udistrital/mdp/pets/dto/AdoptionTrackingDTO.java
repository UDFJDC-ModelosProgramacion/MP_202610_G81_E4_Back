package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import lombok.Data;

@Data
public class AdoptionTrackingDTO {
    private Long id;
    private String frequency;
    private String notes;
    private Date nextReview;
}