package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class AdopterDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String address;
    private String housingType;
    private Boolean hasChildren;
    private Boolean hasOtherPets;
    private List<String> preferences;

    public AdopterDTO(AdopterEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.firstName = entity.getFirstName();
            this.lastName = entity.getLastName();
            this.address = entity.getAddress();
            this.housingType = entity.getHousingType();
            this.hasChildren = entity.getHasChildren();
            this.hasOtherPets = entity.getHasOtherPets();
            this.preferences = entity.getPreferences();
        }
    }
}
