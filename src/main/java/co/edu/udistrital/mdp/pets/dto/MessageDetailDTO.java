package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageDetailDTO extends MessageDTO {

    private UserDTO sender;
    private UserDTO recipient;
    
    public MessageDetailDTO(MessageEntity entity) {
        super(entity);
    }
}
