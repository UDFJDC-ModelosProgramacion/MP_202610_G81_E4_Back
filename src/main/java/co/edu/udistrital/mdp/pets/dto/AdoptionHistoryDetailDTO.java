package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptionHistoryDetailDTO extends AdoptionHistoryDTO {
    private AdoptionDTO adoption;
}