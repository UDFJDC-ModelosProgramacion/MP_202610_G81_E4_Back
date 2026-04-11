package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {

    private Long id;
    private String senderType;
    private String recipientType;
    private LocalDateTime timestamp;
    private String subject;
    private String content;
    private Boolean isRead;
    private Long senderId;
    private Long recipientId;
    
    public MessageDTO(MessageEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.senderType = entity.getSenderType();
            this.recipientType = entity.getRecipientType();
            this.timestamp = entity.getTimestamp();
            this.subject = entity.getSubject();
            this.content = entity.getContent();
            this.isRead = entity.getIsRead();
            this.senderId = entity.getSenderId();
            this.recipientId = entity.getRecipientId();
        }
    }
}