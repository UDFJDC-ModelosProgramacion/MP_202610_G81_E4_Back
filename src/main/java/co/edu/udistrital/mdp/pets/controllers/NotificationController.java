package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDetailDTO;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) 
    public NotificationDTO create(@RequestBody NotificationDTO dto) {
        NotificationEntity entity = toEntity(dto);
        return new NotificationDTO(service.createNotification(entity));
    }

    @GetMapping
    public List<NotificationDTO> findAll() {
        List<NotificationDTO> list = new ArrayList<>();
        for (NotificationEntity e : service.searchNotifications()) {
            list.add(new NotificationDTO(e));
        }
        return list;
    }

    @GetMapping("/{id}")
    public NotificationDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        return new NotificationDetailDTO(service.searchNotification(id));
    }

    @PutMapping("/{id}")
    public NotificationDTO update(@PathVariable Long id, @RequestBody NotificationDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        NotificationEntity entity = toEntity(dto);
        return new NotificationDTO(service.updateNotification(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        service.deleteNotification(id);
    }

    private NotificationEntity toEntity(NotificationDTO dto) {
    if (dto == null) return null;
    
    NotificationEntity e = new NotificationEntity();
    e.setId(dto.getId());
    e.setMessage(dto.getMessage());
    e.setUserType(dto.getUserType());
    e.setNotificationType(dto.getNotificationType());
    e.setRelatedEntity(dto.getRelatedEntity());
    e.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
    e.setTimestamp(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.LocalDateTime.now());
    e.setUserId(dto.getUserId());
    if (dto instanceof NotificationDetailDTO detail) {
        if (detail.getUser() != null && detail.getUser().getId() != null) {
            e.setUserId(detail.getUser().getId());
        }
    }
    return e;
}
}
