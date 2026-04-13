package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.services.NotificationService;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private static final String ERR_MSG = "message";

    @Autowired
    private NotificationService service;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody NotificationDTO dto) {
        try {
            NotificationEntity entity = toEntity(dto);
            NotificationEntity saved = service.createNotification(entity);
            return new ResponseEntity<>(new NotificationDTO(saved), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAll() {
        List<NotificationDTO> list = service.findAll().stream()
                .map(NotificationDTO::new)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            NotificationEntity entity = service.findById(id);
            return ResponseEntity.ok(new NotificationDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Object> markAsRead(@PathVariable Long id) {
        try {
            NotificationEntity entity = service.markAsRead(id);
            return ResponseEntity.ok(new NotificationDTO(entity));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        } catch (IllegalOperationException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        try {
            service.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ERR_MSG, e.getMessage()));
        } catch (IllegalOperationException e) {
            return ResponseEntity.badRequest().body(Map.of(ERR_MSG, e.getMessage()));
        }
    }

    private NotificationEntity toEntity(NotificationDTO dto) {
        if (dto == null) return null;
        NotificationEntity e = new NotificationEntity();
        e.setId(dto.getId());
        e.setUserId(dto.getUserId());
        e.setUserType(dto.getUserType());
        e.setNotificationType(dto.getNotificationType());
        e.setMessage(dto.getMessage());
        e.setRelatedEntity(dto.getRelatedEntity());
        e.setIsRead(Boolean.TRUE.equals(dto.getIsRead()));
        e.setTimestamp(dto.getCreatedAt());
        return e;
    }
}
