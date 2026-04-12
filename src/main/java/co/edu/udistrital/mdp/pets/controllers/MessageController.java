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

import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.entities.Message;
import co.edu.udistrital.mdp.pets.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> create(@RequestBody MessageDTO dto) {
        Message m = toEntity(dto);
        Message saved = messageService.createMessage(m);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageDTO> update(@PathVariable Long id, @RequestBody MessageDTO dto) {
        Message updated = messageService.updateMessage(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long senderId) {
        messageService.deleteMessage(id, senderId);
        return ResponseEntity.noContent().build();
    }

    private MessageDTO toDTO(Message m) {
        return new MessageDTO(
            m.getId(),
            m.getSenderId(),
            m.getRecipientId(),
            m.getSubject(),
            m.getContent(),
            m.getTimestamp(),
            m.getIsRead()
        );
    }

    private Message toEntity(MessageDTO dto) {
        Message m = new Message();
        m.setSenderId(dto.getSenderId());
        m.setRecipientId(dto.getRecipientId());
        m.setSubject(dto.getSubject());
        m.setContent(dto.getContent());
        return m;
    }
}