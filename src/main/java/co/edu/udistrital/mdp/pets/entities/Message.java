package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
        System.out.println("Message sent: " + subject);
    }
    
    public void markAsRead() {
        this.isRead = true;
        System.out.println("Message marked as read");
    }
    
    public void delete() {
        System.out.println("Message ID: " + messageId + " deleted");
    }
    
    public void reply(String response) {
        System.out.println("Replying to message: " + subject);
    }
}