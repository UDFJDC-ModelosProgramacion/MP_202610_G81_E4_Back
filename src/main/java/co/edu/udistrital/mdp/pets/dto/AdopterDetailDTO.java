package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdopterDetailDTO extends AdopterDTO {

    private List<AdoptionRequestDTO> adoptionRequests;
    private List<AdoptionDTO> adoptions;

    public AdopterDetailDTO(AdopterEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getAdoptionRequests() != null) {
                this.adoptionRequests = entity.getAdoptionRequests().stream()
                        .map(AdoptionRequestDTO::new)
                        .toList();
            }
            if (entity.getAdoptions() != null) {
                this.adoptions = entity.getAdoptions().stream()
                        .map(AdoptionDTO::new)
                        .toList();
            }
        }
    }
}
