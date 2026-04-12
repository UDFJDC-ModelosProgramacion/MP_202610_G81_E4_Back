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
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity extends BaseEntity {

    @Column(name = "sender_type")
    private String senderType;

    @Column(name = "recipient_type")
    private String recipientType;

    private LocalDateTime timestamp;
    
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isRead;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "recipient_id")
    private Long recipientId;
}