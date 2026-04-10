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
import co.edu.udistrital.mdp.pets.services.NotificationService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(NotificationService.class)
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<NotificationEntity> notificationList = new ArrayList<>();
    private UserEntity commonUser;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from NotificationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
    }

    private void insertData() {
        // Crear un usuario común para las notificaciones
        commonUser = factory.manufacturePojo(UserEntity.class);
        entityManager.persist(commonUser);

        for (int i = 0; i < 3; i++) {
            NotificationEntity notification = factory.manufacturePojo(NotificationEntity.class);
            notification.setUser(commonUser);
            notification.setMessage("Mensaje de prueba " + i);
            notification.setNotificationType("INFO");
            notification.setUserType("ADOPTER");
            
            // Seteamos una fecha de hace 40 días para permitir pruebas de borrado
            notification.setTimestamp(LocalDateTime.now().minusDays(40));
            notification.setIsRead(false);

            entityManager.persist(notification);
            notificationList.add(notification);
        }
    }

    @Test
    void testCreateNotification() {
        NotificationEntity newEntity = factory.manufacturePojo(NotificationEntity.class);
        newEntity.setUser(commonUser);
        newEntity.setMessage("Nueva notificación importante");

        NotificationEntity result = notificationService.createNotification(newEntity);
        
        assertNotNull(result);
        NotificationEntity entity = entityManager.find(NotificationEntity.class, result.getId());
        assertEquals(newEntity.getMessage(), entity.getMessage());
        assertFalse(entity.getIsRead());
        assertNotNull(entity.getTimestamp());
    }

    @Test
    void testCreateNotificationNoUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationEntity entity = factory.manufacturePojo(NotificationEntity.class);
            entity.setUser(null);
            notificationService.createNotification(entity);
        });
    }

    @Test
    void testCreateNotificationEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationEntity entity = factory.manufacturePojo(NotificationEntity.class);
            entity.setUser(commonUser);
            entity.setMessage("");
            notificationService.createNotification(entity);
        });
    }

    @Test
    void testSearchNotification() {
        NotificationEntity entity = notificationList.get(0);
        NotificationEntity result = notificationService.searchNotification(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getMessage(), result.getMessage());
    }

    @Test
    void testSearchNotificationNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            notificationService.searchNotification(999L);
        });
    }

    @Test
    void testUpdateNotification() {
        NotificationEntity entity = notificationList.get(0);
        NotificationEntity newEntity = factory.manufacturePojo(NotificationEntity.class);
        newEntity.setIsRead(true);
        newEntity.setMessage("Mensaje Actualizado");

        NotificationEntity result = notificationService.updateNotification(entity.getId(), newEntity);
        
        assertNotNull(result);
        NotificationEntity updated = entityManager.find(NotificationEntity.class, entity.getId());
        assertTrue(updated.getIsRead());
        assertEquals("Mensaje Actualizado", updated.getMessage());
    }

    @Test
    void testDeleteNotificationYoungerThan30Days() {
        // Creamos una notificación nueva (de hoy)
        NotificationEntity recent = factory.manufacturePojo(NotificationEntity.class);
        recent.setUser(commonUser);
        recent.setTimestamp(LocalDateTime.now());
        entityManager.persist(recent);

        assertThrows(IllegalStateException.class, () -> {
            notificationService.deleteNotification(recent.getId());
        });
    }

    @Test
    void testDeleteNotificationSuccess() {
        // Usamos una de la lista que tiene 40 días de antigüedad (seteadas en insertData)
        NotificationEntity oldNotification = notificationList.get(0);
        
        notificationService.deleteNotification(oldNotification.getId());

        NotificationEntity deleted = entityManager.find(NotificationEntity.class, oldNotification.getId());
        assertNull(deleted);
    }
}