package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public MessageEntity createMessage(MessageEntity message) {
        Long senderId = extractUserId(message, "Sender");
        Long recipientId = extractUserId(message, "Recipient");

        if (senderId == null || recipientId == null) {
            throw new IllegalArgumentException("Sender and recipient are required");
        }

        if (senderId.equals(recipientId)) {
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

    @Transactional
    public MessageEntity updateMessage(Long id, MessageEntity updatedMessage) {
        MessageEntity existing = messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        Long existingSenderId = extractUserId(existing, "Sender");
        Long updatedSenderId = extractUserId(updatedMessage, "Sender");
        if (existingSenderId == null || !existingSenderId.equals(updatedSenderId)) {
            throw new IllegalArgumentException("Only sender can edit message");
        }

        long minutesSinceSent = ChronoUnit.MINUTES.between(
            existing.getTimestamp(), 
            LocalDateTime.now()
        );
        
        if (minutesSinceSent > 15) {
            throw new IllegalArgumentException("Cannot edit message after 15 minutes");
        }

        if (Boolean.TRUE.equals(existing.getIsRead())) {
            throw new IllegalArgumentException("Cannot edit read messages");
        }

        Long existingRecipientId = extractUserId(existing, "Recipient");
        Long updatedRecipientId = extractUserId(updatedMessage, "Recipient");
        if (existingRecipientId == null || !existingRecipientId.equals(updatedRecipientId)) {
            throw new IllegalArgumentException("Cannot change recipient");
        }

        existing.setSubject(updatedMessage.getSubject());
        existing.setContent(updatedMessage.getContent());

        return messageRepository.save(existing);
    }

    @Transactional
    public void deleteMessage(Long id, Long requesterId) {
        MessageEntity message = messageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        Long senderId = extractUserId(message, "Sender");
        if (senderId == null || !senderId.equals(requesterId)) {
            throw new IllegalArgumentException("Only sender can delete message");
        }

        messageRepository.deleteById(id);
    }

    private Long extractUserId(MessageEntity message, String role) {
        if (message == null) {
            return null;
        }

        try {
            Method directIdGetter = MessageEntity.class.getMethod("get" + role + "Id");
            Object value = directIdGetter.invoke(message);
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        } catch (Exception ignored) {
            // Fallback to relation-based getter
        }

        try {
            Method relationGetter = MessageEntity.class.getMethod("get" + role);
            Object relation = relationGetter.invoke(message);
            if (relation == null) {
                return null;
            }

            Method idGetter = relation.getClass().getMethod("getId");
            Object id = idGetter.invoke(relation);
            if (id instanceof Number) {
                return ((Number) id).longValue();
            }
        } catch (Exception ignored) {
            return null;
        }

        return null;
    }
}