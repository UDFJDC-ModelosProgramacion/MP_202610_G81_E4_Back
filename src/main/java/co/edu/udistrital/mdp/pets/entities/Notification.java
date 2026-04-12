package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {
    
    @Column(name = "user_id")
    private Long userId;
    
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
    
    // ==================== RELACIONES ====================
    
    // Relación con Usuario
    /*
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "user_id")
private User user;
*/
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (isRead == null) {
            isRead = false;
        }
    }
}