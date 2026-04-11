package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public UserDTO(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.email = entity.getEmail();
            this.phone = entity.getPhone();
        }
    }
}
