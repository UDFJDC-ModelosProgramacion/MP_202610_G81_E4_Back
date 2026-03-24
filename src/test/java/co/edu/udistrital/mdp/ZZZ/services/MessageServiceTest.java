package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.Message;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;
import co.edu.udistrital.mdp.pets.services.MessageService;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    @Test
    void shouldCreateMessageSuccessfully() {

        Message message = new Message();
        message.setSenderId(1L);
        message.setRecipientId(2L);
        message.setSubject("Hola");
        message.setContent("Mensaje de prueba");

        when(messageRepository.save(any(Message.class)))
                .thenReturn(message);

        Message result = messageService.createMessage(message);

        assertNotNull(result.getTimestamp());
        assertFalse(result.getIsRead());
        verify(messageRepository).save(message);
    }

    @Test
    void shouldFailWhenSendingMessageToYourself() {

        Message message = new Message();
        message.setSenderId(1L);
        message.setRecipientId(1L);
        message.setSubject("Hola");

        assertThrows(
                IllegalArgumentException.class,
                () -> messageService.createMessage(message)
        );
    }
}