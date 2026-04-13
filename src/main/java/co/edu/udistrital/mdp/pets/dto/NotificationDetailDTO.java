package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationDetailDTO extends NotificationDTO {

    private UserDTO user;

    public NotificationDetailDTO(NotificationEntity entity) {
        super(entity);
    }
}
