package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private Long userId;           
    private String userType;      
    private String notificationType; 
    private String message;
    private String relatedEntity;   
    private Boolean isRead;
    private LocalDateTime createdAt;

      public NotificationDTO(NotificationEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.userId = entity.getUserId();
            this.userType = entity.getUserType();
            this.notificationType = entity.getNotificationType();
            this.message = entity.getMessage();
            this.relatedEntity = entity.getRelatedEntity();
            this.isRead = entity.getIsRead();
            this.createdAt = entity.getTimestamp();
        }
    }
}
