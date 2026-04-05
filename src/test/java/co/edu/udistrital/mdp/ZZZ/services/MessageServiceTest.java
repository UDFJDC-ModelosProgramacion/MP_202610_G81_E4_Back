package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MessageService.class)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MessageEntity> messageList = new ArrayList<>();
    private UserEntity sender;
    private UserEntity recipient;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MessageEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
    }

    private void insertData() {
        sender = factory.manufacturePojo(UserEntity.class);
        entityManager.persist(sender);
        
        recipient = factory.manufacturePojo(UserEntity.class);
        entityManager.persist(recipient);

        for (int i = 0; i < 3; i++) {
            MessageEntity message = factory.manufacturePojo(MessageEntity.class);
            message.setSender(sender);
            message.setRecipient(recipient);
            message.setSubject("Subject " + i);
            message.setIsRead(false);
            message.setTimestamp(LocalDateTime.now());
            
            entityManager.persist(message);
            messageList.add(message);
        }
    }

    @Test
    void testCreateMessage() {
        MessageEntity newEntity = factory.manufacturePojo(MessageEntity.class);
        newEntity.setSender(sender);
        newEntity.setRecipient(recipient);
        newEntity.setSubject("Test Subject");
        newEntity.setContent("Test Content");

        MessageEntity result = messageService.createMessage(newEntity);
        
        assertNotNull(result);
        MessageEntity found = entityManager.find(MessageEntity.class, result.getId());
        assertEquals("Test Subject", found.getSubject());
        assertFalse(found.getIsRead());
        assertNotNull(found.getTimestamp());
    }

    @Test
    void testCreateMessageToYourself() {
        assertThrows(IllegalArgumentException.class, () -> {
            MessageEntity message = factory.manufacturePojo(MessageEntity.class);
            message.setSender(sender);
            message.setRecipient(sender);
            messageService.createMessage(message);
        });
    }

    @Test
    void testUpdateMessageSuccess() {
        MessageEntity existing = messageList.get(0);
        MessageEntity updateData = new MessageEntity();
        updateData.setSender(sender);
        updateData.setRecipient(recipient);
        updateData.setSubject("Updated Subject");
        updateData.setContent("Updated Content");

        MessageEntity result = messageService.updateMessage(existing.getId(), updateData);
        
        assertNotNull(result);
        MessageEntity updated = entityManager.find(MessageEntity.class, existing.getId());
        assertEquals("Updated Subject", updated.getSubject());
    }

    @Test
    void testUpdateMessageAfter15Minutes() {
        MessageEntity oldMessage = messageList.get(1);
        oldMessage.setTimestamp(LocalDateTime.now().minusMinutes(20));
        entityManager.persist(oldMessage);
        entityManager.flush();

        MessageEntity updateData = new MessageEntity();
        updateData.setSender(sender);
        
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.updateMessage(oldMessage.getId(), updateData);
        });
    }

    @Test
    void testUpdateMessageAlreadyRead() {
        MessageEntity readMessage = messageList.get(2);
        readMessage.setIsRead(true);
        entityManager.persist(readMessage);
        
        MessageEntity updateData = new MessageEntity();
        updateData.setSender(sender);

        assertThrows(IllegalArgumentException.class, () -> {
            messageService.updateMessage(readMessage.getId(), updateData);
        });
    }

    @Test
    void testDeleteMessageBySender() {
        MessageEntity message = messageList.get(0);
        
        messageService.deleteMessage(message.getId(), sender.getId());
        
        MessageEntity found = entityManager.find(MessageEntity.class, message.getId());
        assertNull(found);
    }

    @Test
    void testDeleteMessageByNonSender() {
        MessageEntity message = messageList.get(0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.deleteMessage(message.getId(), recipient.getId());
        });
    }
}