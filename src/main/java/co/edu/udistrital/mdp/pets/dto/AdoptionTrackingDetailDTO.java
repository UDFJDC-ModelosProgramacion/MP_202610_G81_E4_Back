package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptionTrackingDetailDTO extends AdoptionTrackingDTO {
    private AdoptionDTO adoption;
}