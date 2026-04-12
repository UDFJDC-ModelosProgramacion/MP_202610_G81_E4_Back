package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDetailDTO {
    private Long id;
    private String subject;
    private String content;
    private LocalDateTime timestamp;
    private Boolean isRead;
    
    // Objetos completos en lugar de solo IDs
    private UserDTO sender;
    private UserDTO recipient;
}

// UserDTO básico para las relaciones
@Data
@NoArgsConstructor
@AllArgsConstructor
class UserDTO {
    private Long id;
    private String name;
    private String email;
}