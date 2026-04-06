package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PetDetailDTO extends PetDTO {

    private List<VaccinationRecordDTO> vaccinationRecords;
    private ShelterDTO shelter;

}