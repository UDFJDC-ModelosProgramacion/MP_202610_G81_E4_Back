package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterDTO {
    
    private Long id;
    private String name;
    private String city;
    private String address;
    private String phone;
    private String email;

    public ShelterDTO(ShelterEntity entity) {
    this.id = entity.getId();
    this.name = entity.getName();
    this.city = entity.getCity();
    this.address = entity.getAddress();
    this.phone = entity.getPhone();
    this.email = entity.getEmail();
    }
}