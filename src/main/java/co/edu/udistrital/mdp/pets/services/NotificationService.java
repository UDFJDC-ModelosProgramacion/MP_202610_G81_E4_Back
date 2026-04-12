package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
    private static final String NOTIF_NOT_FOUND = "Notification not found with id: ";

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notification) {
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
        return notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public NotificationEntity findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        return notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOTIF_NOT_FOUND + id));
    }

    @Transactional(readOnly = true)
    public List<NotificationEntity> findAll() {
        return notificationRepository.findAll().stream().toList();
    }

    @Transactional
    public NotificationEntity updateNotification(Long id, NotificationEntity notification) {
        if (id == null || notification == null) {
            throw new IllegalArgumentException("Id and Notification object cannot be null");
        }
        NotificationEntity existing = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOTIF_NOT_FOUND + id));
        
        existing.setIsRead(notification.getIsRead());
        existing.setMessage(notification.getMessage());
        existing.setNotificationType(notification.getNotificationType());
        existing.setUserType(notification.getUserType());
        existing.setRelatedEntity(notification.getRelatedEntity());
        return notificationRepository.save(existing);
    }

    @Transactional
    public NotificationEntity markAsRead(Long id) throws IllegalOperationException {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOTIF_NOT_FOUND + id));
        
        if (Boolean.TRUE.equals(notification.getIsRead())) {
            throw new IllegalOperationException("La notificación ya ha sido leída.");
        }
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void deleteNotification(Long id) throws IllegalOperationException {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(NOTIF_NOT_FOUND + id));
        
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        if (notification.getTimestamp().isAfter(thirtyDaysAgo)) {
            throw new IllegalOperationException("No se pueden eliminar notificaciones con menos de 30 días de antigüedad");
        }
        notificationRepository.delete(notification);
    }
}