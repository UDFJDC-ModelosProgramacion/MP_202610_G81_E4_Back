package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@EqualsAndHashCode(callSuper = true)
public class MessageEntity extends BaseEntity {
     
    private String senderType;
    private String recipientType;
    private LocalDateTime timestamp;
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private Boolean isRead = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id") 
    private UserEntity sender;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private UserEntity recipient;
}