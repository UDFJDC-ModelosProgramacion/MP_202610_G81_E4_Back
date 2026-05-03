package co.edu.udistrital.mdp.pets.services;

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
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MessageService.class)
class MessageServiceTest { 

    @Autowired
    private MessageService messageService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MessageEntity> messageList = new ArrayList<>();
    private Long senderId = 1L;
    private Long recipientId = 2L;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MessageEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MessageEntity message = factory.manufacturePojo(MessageEntity.class);
            message.setSenderId(senderId);
            message.setRecipientId(recipientId);
            message.setSenderType("ADOPTER");
            message.setRecipientType("SHELTER");
            message.setSubject("Subject " + i);
            message.setIsRead(false);
            message.setTimestamp(LocalDateTime.now());
            
            entityManager.persist(message);
            messageList.add(message);
        }
        entityManager.flush();
    }

    @Test
    void testCreateMessage(){
        MessageEntity newEntity = factory.manufacturePojo(MessageEntity.class);
        newEntity.setSenderId(senderId);
        newEntity.setRecipientId(recipientId);
        newEntity.setSubject("Test Subject");
        newEntity.setContent("Test Content");

        MessageEntity result = messageService.createMessage(newEntity);
        
        assertNotNull(result);
        MessageEntity found = entityManager.find(MessageEntity.class, result.getId());
        assertEquals("Test Subject", found.getSubject());
    }
    @Test
    void testCreateMessageWithSenderIdNull(){
        MessageEntity newEntity = factory.manufacturePojo(MessageEntity.class);
        newEntity.setSenderId(null);
        newEntity.setRecipientId(recipientId);
        newEntity.setSubject("Test Subject");
        newEntity.setContent("Test Content");
        assertThrows(IllegalArgumentException.class, () ->{
            messageService.createMessage(newEntity);
        });
    }
    @Test 
    void testCreateMessageWithSubjectNull(){
        MessageEntity newEntity = factory.manufacturePojo(MessageEntity.class);
        newEntity.setSenderId(senderId);
        newEntity.setRecipientId(recipientId);
        newEntity.setSubject(null);
        newEntity.setContent("Test Content");
        assertThrows(IllegalArgumentException.class, () ->{
            messageService.createMessage(newEntity);
        });
    }
    @Test
    void testCreateMessageWithContentinvalid(){
        MessageEntity newEntity = factory.manufacturePojo(MessageEntity.class);
        newEntity.setSenderId(senderId);
        newEntity.setRecipientId(recipientId);
        newEntity.setSubject("Test Subject");
        newEntity.setContent("a".repeat(1001));
        assertThrows(IllegalArgumentException.class, () ->{
            messageService.createMessage(newEntity);
        });
    }

    @Test
    void testSearchMessage() {
        MessageEntity expected = messageList.get(0);
        MessageEntity result = messageService.searchMessage(expected.getId());
        
        assertNotNull(result);
        assertEquals(expected.getSubject(), result.getSubject());
    }

    @Test
    void testSearchMessageNotFound() {
        assertThrows(RuntimeException.class, () -> messageService.searchMessage(999L));
    }
    @Test
    void testSearchMessageWithIdNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.searchMessage(null);
        });
    }

    @Test
    void testSearchAllMessages() {
        List<MessageEntity> list = messageService.searchAllMessages();
        assertEquals(messageList.size(), list.size());
    }

    @Test
    void testCreateMessageToYourself() {
        MessageEntity message = factory.manufacturePojo(MessageEntity.class);
        message.setSenderId(senderId);
        message.setRecipientId(senderId);
        assertThrows(IllegalArgumentException.class, () -> messageService.createMessage(message));
    }

    @Test
    void testUpdateMessageSuccess() {
        MessageEntity existing = messageList.get(0);
        MessageEntity updateData = new MessageEntity();
        updateData.setSenderId(senderId); 
        updateData.setRecipientId(recipientId);
        updateData.setSubject("Updated Subject");
        updateData.setContent("Updated Content");

        MessageEntity result = messageService.updateMessage(existing.getId(), updateData);
        
        assertNotNull(result);
        assertEquals("Updated Subject", result.getSubject());
    }
    @Test
    void testUpdateMessageDifferentSender(){
        MessageEntity existing = messageList.get(0);
        MessageEntity updateData = new MessageEntity();
        Long existingId = existing.getId();
        updateData.setSenderId(3L); 
        updateData.setRecipientId(recipientId);
        updateData.setSubject("Updated Subject");
        updateData.setContent("Updated Content");
        assertThrows(IllegalArgumentException.class, () ->{
            messageService.updateMessage(existingId,updateData);
        });
    }

    @Test
    void testUpdateMessageAfter15Minutes() {
        MessageEntity oldMessage = messageList.get(1);
        oldMessage.setTimestamp(LocalDateTime.now().minusMinutes(20));
        entityManager.persist(oldMessage);
        entityManager.flush();

        MessageEntity updateData = new MessageEntity();
        updateData.setSenderId(senderId);
        updateData.setRecipientId(recipientId);
        Long idToUpdate = oldMessage.getId(); 
        assertThrows(IllegalArgumentException.class, () -> messageService.updateMessage(idToUpdate, updateData));
    }
    @Test 
    void testUpdateMessageRead(){
        MessageEntity message = messageList.get(0);
        MessageEntity update = new MessageEntity();
        message.setIsRead(true);
        entityManager.persist(message);
        entityManager.flush();
        entityManager.clear();
        update.setSenderId(senderId); 
        update.setRecipientId(recipientId);
        update.setSubject("Updated Subject");
        update.setContent("Updated Content");
        Long messageId = message.getId();
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.updateMessage(messageId, update);
        });
    }
    @Test
    void testUpdateMessageDifferentRecipient(){
        MessageEntity existing = messageList.get(0);
        MessageEntity updateData = new MessageEntity();
        updateData.setSenderId(senderId); 
        updateData.setRecipientId(4L);
        updateData.setSubject("Updated Subject");
        updateData.setContent("Updated Content");
        Long existingId = existing.getId();
        assertThrows(IllegalArgumentException.class, () ->{
            messageService.updateMessage(existingId,updateData);
        });
    }

    @Test
    void testDeleteMessageBySender() {
        MessageEntity message = messageList.get(0);
        messageService.deleteMessage(message.getId(), senderId);
        
        entityManager.flush();
        MessageEntity found = entityManager.find(MessageEntity.class, message.getId());
        assertNull(found);
    }
    @Test
    void testDeleteMessageWrongSender(){
        MessageEntity message = messageList.get(0);
        Long messageId = message.getId();
        assertThrows(IllegalArgumentException.class, () -> {
            messageService.deleteMessage(messageId, 3L);
        });
    }
    @Test
    void testDeleteMessageNullId(){
        assertThrows(RuntimeException.class, () -> {
            messageService.deleteMessage(999L, senderId);
        });
    }
}