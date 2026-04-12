package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VeterinarianDetailDTO extends VeterinarianDTO {

    private Long shelterId;
    private String shelterName;
    private List<MedicalEventDTO> medicalEvents;

    public VeterinarianDetailDTO(VeterinarianEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
                this.shelterName = entity.getShelter().getName();
            }
            
            if (entity.getMedicalEvents() != null) {
                this.medicalEvents = entity.getMedicalEvents().stream()
                        .map(MedicalEventDTO::new)
                        .collect(Collectors.toList());
            }
        }
    }
}
