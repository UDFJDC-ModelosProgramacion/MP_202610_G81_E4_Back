package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.entities.Notification;
import co.edu.udistrital.mdp.pets.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDTO> create(@RequestBody NotificationDTO dto) {
        Notification n = toEntity(dto);
        Notification saved = notificationService.createNotification(n);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> update(@PathVariable Long id, @RequestBody NotificationDTO dto) {
        Notification updated = notificationService.updateNotification(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long userId) {
        notificationService.deleteNotification(id, userId);
        return ResponseEntity.noContent().build();
    }

    private NotificationDTO toDTO(Notification n) {
        return new NotificationDTO(
            n.getId(),
            n.getUserId(),
            n.getNotificationType(),
            n.getMessage(),
            n.getTimestamp(),
            n.getIsRead()
        );
    }

    private Notification toEntity(NotificationDTO dto) {
    Notification n = new Notification();
    n.setUserId(dto.getUserId());
    n.setNotificationType(dto.getNotificationType());
    n.setMessage(dto.getMessage());
    n.setTimestamp(dto.getTimestamp());
    n.setIsRead(dto.getIsRead());
    return n;
}
}