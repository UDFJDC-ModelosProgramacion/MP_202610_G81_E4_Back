package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PetDetailDTO extends PetDTO {

    private List<VaccinationRecordDTO> vaccinationRecords;
    private ShelterDTO shelter;

    public PetDetailDTO(PetEntity entity) {
        super(entity); 
        if (entity != null) {
            // Mapeo del refugio completo
            if (entity.getShelter() != null) {
                this.shelter = new ShelterDTO(entity.getShelter());
            }
            if (entity.getVaccinationRecords() != null) {
                this.vaccinationRecords = entity.getVaccinationRecords().stream()
                        .map(VaccinationRecordDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }
}