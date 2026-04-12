package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long recipientId;
    private String subject;
    private String content;
    private LocalDateTime timestamp;
    private Boolean isRead;
}