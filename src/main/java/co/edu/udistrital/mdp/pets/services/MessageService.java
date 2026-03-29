package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.Message;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    public Message createMessage(Message message) {
        if (message.getSenderId() == null || message.getRecipientId() == null) {
            throw new IllegalArgumentException("Sender and recipient are required");
        }
        
        if (message.getSenderId().equals(message.getRecipientId())) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }
        
        if (message.getSubject() == null || message.getSubject().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty");
        }
        
        if (message.getContent() != null && message.getContent().length() > 1000) {
            throw new IllegalArgumentException("Message content too long (max 1000 characters)");
        }
        
        message.setTimestamp(LocalDateTime.now());
        message.setIsRead(false);
        
        return messageRepository.save(message);
    }
    
    public Message updateMessage(Long id, Message updatedMessage) {
        Message existing = messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!existing.getSenderId().equals(updatedMessage.getSenderId())) {
            throw new IllegalArgumentException("Only sender can edit message");
        }

        long minutesSinceSent = ChronoUnit.MINUTES.between(
            existing.getTimestamp(), 
            LocalDateTime.now()
        );
        if (minutesSinceSent > 15) {
            throw new IllegalArgumentException("Cannot edit message after 15 minutes");
        }

        if (existing.getIsRead()) {
            throw new IllegalArgumentException("Cannot edit read messages");
        }
 
        if (!existing.getRecipientId().equals(updatedMessage.getRecipientId())) {
            throw new IllegalArgumentException("Cannot change recipient");
        }
        
        existing.setSubject(updatedMessage.getSubject());
        existing.setContent(updatedMessage.getContent());
        
        return messageRepository.save(existing);
    }
    
    public void deleteMessage(Long id, Long requesterId) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSenderId().equals(requesterId)) {
            throw new IllegalArgumentException("Only sender can delete message");
        }
        
        messageRepository.deleteById(id);
    }
}