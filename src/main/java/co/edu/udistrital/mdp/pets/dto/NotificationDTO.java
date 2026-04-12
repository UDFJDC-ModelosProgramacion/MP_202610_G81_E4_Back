package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String notificationType;
    private String message;
    private LocalDateTime timestamp;
    private Boolean isRead;
}