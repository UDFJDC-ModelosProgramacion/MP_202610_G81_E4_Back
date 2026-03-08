package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByUserId(Long userId);
    
    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
    
    List<Notification> findByUserIdOrderByTimestampDesc(Long userId);
    
    List<Notification> findByNotificationType(String notificationType);
    
    List<Notification> findByUserIdAndNotificationType(Long userId, String notificationType);
    
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
    
    void deleteByUserId(Long userId);
}