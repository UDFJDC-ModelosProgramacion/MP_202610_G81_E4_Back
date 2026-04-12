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

import co.edu.udistrital.mdp.pets.entities.Notification;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.services.NotificationService;

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
        n.setUserType("ADOPTION");

        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(n);

        Notification result = notificationService.createNotification(n);

        assertNotNull(result.getTimestamp());
        assertFalse(result.getIsRead());

        verify(notificationRepository).save(n);
    }

    @Test
    void shouldFailWhenTypeInvalid() {

        Notification n = new Notification();
        n.setUserId(1L);
        n.setMessage("Test notification");
        n.setUserType("INVALID");

        assertThrows(
                IllegalArgumentException.class,
                () -> notificationService.createNotification(n)
        );
    }
}