package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        if (notification.getUser() == null) {
            throw new IllegalArgumentException("Notification must be assigned to a user");
        }
        if (notification.getMessage() == null || notification.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Notification message cannot be empty");
        }

        notification.setTimestamp(LocalDateTime.now());
        notification.setIsRead(false);

        NotificationEntity savedNotification = notificationRepository.save(notification);
        log.info("Notification created with id: {} for user: {}", savedNotification.getId(), notification.getUser().getId());
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

        NotificationEntity updatedNotification = notificationRepository.save(existing);
        log.info("Notification updated with id: {}", updatedNotification.getId());
        return updatedNotification;
    }

    @Transactional
    public void deleteNotification(Long id) {
        log.info("Deleting Notification with id: {}", id);
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        long daysSinceCreation = ChronoUnit.DAYS.between(notification.getTimestamp(), LocalDateTime.now());
        if (daysSinceCreation < 30) {
            throw new IllegalStateException("Cannot delete notifications younger than 30 days");
        }

        notificationRepository.delete(notification);
        log.info("Notification deleted successfully");
    }
}