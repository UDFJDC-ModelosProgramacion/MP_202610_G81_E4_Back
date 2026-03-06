package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Integer messageId;
    
    @Column(name = "sender_id", nullable = false)
    private Integer senderId;
    
    @Column(name = "sender_type", nullable = false, length = 50)
    private String senderType;
    
    @Column(name = "recipient_id", nullable = false)
    private Integer recipientId;
    
    @Column(name = "recipient_type", nullable = false, length = 50)
    private String recipientType;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false, length = 200)
    private String subject;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = false;
        }
    }
    
    public void send() {
        log.info("Message sent: {}", subject);
    }
    
    public void markAsRead() {
        this.isRead = true;
        log.debug("Message marked as read");
    }
    
    public void delete() {
        log.info("Message ID: {} deleted", messageId);
    }
    
    public void reply(String response) {
        log.info("Replying to message: {}", subject);
    }
}