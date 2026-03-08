package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
    List<NotificationEntity> findByUserId(Long userId);
    
    List<NotificationEntity> findByUserIdAndIsRead(Long userId, Boolean isRead);
    
    List<NotificationEntity> findByUserIdOrderByTimestampDesc(Long userId);
    
    List<NotificationEntity> findByNotificationType(String notificationType);
    
    List<NotificationEntity> findByUserIdAndNotificationType(Long userId, String notificationType);
    
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
    
    void deleteByUserId(Long userId);
}