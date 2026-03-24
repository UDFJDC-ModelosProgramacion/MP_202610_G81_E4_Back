package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.Notification;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldCreateNotificationSuccessfully() {

        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Nueva adopción");
        n.setType("ADOPTION");

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(n);

        Notification result = notificationService.createNotification(n);

        assertNotNull(result.getTimestamp());
        assertFalse(result.getRead());

        verify(notificationRepository).save(n);
    }

    @Test
    void shouldFailWhenTypeInvalid() {

        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Test notification");
        n.setType("INVALID");

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(n)
        );
    }
}