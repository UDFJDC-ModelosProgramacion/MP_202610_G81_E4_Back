package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class VaccinationRecordDetailDTO extends VaccinationRecordDTO {

    private PetDTO pet;
    private List<VaccineDTO> vaccines;

}