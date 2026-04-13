package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class VeterinarianDTO {

    private Long id;
    private Long veterinarianIdBusiness;
    private String lastName;
    private List<String> specialties;
    private String availability;
    private Long shelterId;

    public VeterinarianDTO(VeterinarianEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.veterinarianIdBusiness = entity.getVeterinarianIdBusiness();
            this.lastName = entity.getLastName();
            this.specialties = entity.getSpecialties();
            this.availability = entity.getAvailability();
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
            }
        }
    }
}
