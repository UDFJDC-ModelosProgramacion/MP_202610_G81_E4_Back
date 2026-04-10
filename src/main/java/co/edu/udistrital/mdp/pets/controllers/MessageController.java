package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService service;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MessageDTO dto) {
        try {
            MessageEntity entity = toEntity(dto);
            MessageEntity saved = service.createMessage(entity);
            return new ResponseEntity<>(new MessageDTO(saved), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public List<MessageDTO> findAll() {
        List<MessageDTO> list = new ArrayList<>();
        for (MessageEntity m : service.searchAllMessages()) {
            list.add(new MessageDTO(m));
        }
        return list;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new MessageDTO(service.searchMessage(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody MessageDTO dto) {
        try {
            MessageEntity entity = toEntity(dto);
            return ResponseEntity.ok(new MessageDTO(service.updateMessage(id, entity)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) { 
        try {
            service.deleteMessage(id, id); 
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    private MessageEntity toEntity(MessageDTO dto) {
        if (dto == null) return null;
        
        MessageEntity e = new MessageEntity();
        e.setId(dto.getId());
        e.setSubject(dto.getSubject());
        e.setContent(dto.getContent());
        e.setIsRead(dto.getIsRead() != null ? dto.getIsRead() : false);
        e.setTimestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now());
        e.setSenderId(dto.getSenderId());
        e.setRecipientId(dto.getRecipientId());
        e.setSenderType(dto.getSenderType());
        e.setRecipientType(dto.getRecipientType());

        return e;
    }
}