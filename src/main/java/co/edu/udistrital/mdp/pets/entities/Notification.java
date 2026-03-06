package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer notificationId;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    
    @Column(name = "user_type", nullable = false, length = 50)
    private String userType;
    
    @Column(name = "notification_type", nullable = false, length = 50)
    private String notificationType;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private Boolean isRead = false;
    
    @Column(name = "related_entity", length = 100)
    private String relatedEntity;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = false;
        }
    }
    
    public void create() {
        log.info("Notification created: {}", notificationType);
    }
    
    public void send() {
        log.info("Sending notification to user ID: {}", userId);
    }
    
    public void markAsRead() {
        this.isRead = true;
        log.debug("Notification marked as read");
    }
    
    public void delete() {
        log.info("Notification ID: {} deleted", notificationId);
    }
}