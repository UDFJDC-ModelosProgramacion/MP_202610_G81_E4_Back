package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDetailDTO extends UserDTO {

    private List<NotificationDTO> notifications;

    public UserDetailDTO(UserEntity entity) {
        super(entity);
       
    }
}
