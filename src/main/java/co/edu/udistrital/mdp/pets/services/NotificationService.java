package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notification) {
        log.info("Creating Notification");
        
        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }
        
        if (notification.getUserId() == null) {
            throw new IllegalArgumentException("Notification must be assigned to a user ID");
        }
        
        if (notification.getMessage() == null || notification.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be empty");
        }

        if (notification.getTimestamp() == null) {
            notification.setTimestamp(LocalDateTime.now());
        }
        if (notification.getIsRead() == null) {
            notification.setIsRead(false);
        }

        NotificationEntity savedNotification = notificationRepository.save(notification);
        log.info("Notification created with id: {} for user id: {}", savedNotification.getId(), notification.getUserId());
        return savedNotification;
    }

    @Transactional(readOnly = true)
    public NotificationEntity searchNotification(Long id) {
        log.info("Searching Notification with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<NotificationEntity> searchNotifications() {
        log.info("Searching all notifications");
        return notificationRepository.findAll();
    }

    @Transactional
    public NotificationEntity updateNotification(Long id, NotificationEntity notification) {
        log.info("Updating Notification with id: {}", id);
        
        if (id == null || notification == null) {
            throw new IllegalArgumentException("Id and Notification object cannot be null");
        }

        NotificationEntity existing = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        existing.setIsRead(notification.getIsRead());
        existing.setMessage(notification.getMessage());
        existing.setNotificationType(notification.getNotificationType());
        existing.setUserType(notification.getUserType());
        existing.setRelatedEntity(notification.getRelatedEntity());
        return notificationRepository.save(existing);
    }

    @Transactional
    public void deleteNotification(Long id) {
        NotificationEntity notification = searchNotification(id);

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
    
        if (notification.getTimestamp().isAfter(thirtyDaysAgo)) {
            throw new IllegalStateException("No se pueden eliminar notificaciones con menos de 30 días de antigüedad");
     }

    notificationRepository.delete(notification);
    }
}