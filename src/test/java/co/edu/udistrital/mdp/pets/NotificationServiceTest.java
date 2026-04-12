package co.edu.udistrital.mdp.pets;

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
    
    // Usamos un ID de prueba para simular al usuario
    private final Long commonUserId = 1L;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from NotificationEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            NotificationEntity notification = factory.manufacturePojo(NotificationEntity.class);
            // Seteamos el ID plano en lugar del objeto UserEntity
            notification.setUserId(commonUserId);
            notification.setMessage("Mensaje de prueba " + i);
            notification.setNotificationType("INFO");
            notification.setUserType("ADOPTER");
            
            // Fecha de hace 40 días para pasar la regla del delete
            notification.setTimestamp(LocalDateTime.now().minusDays(40));
            notification.setIsRead(false);

            entityManager.persist(notification);
            notificationList.add(notification);
        }
        entityManager.flush();
    }

    @Test
    void testCreateNotification() {
        NotificationEntity newEntity = factory.manufacturePojo(NotificationEntity.class);
        newEntity.setUserId(commonUserId);
        newEntity.setMessage("Nueva notificación importante");
        newEntity.setTimestamp(LocalDateTime.now());

        NotificationEntity result = notificationService.createNotification(newEntity);
        
        assertNotNull(result);
        NotificationEntity entity = entityManager.find(NotificationEntity.class, result.getId());
        assertEquals(newEntity.getMessage(), entity.getMessage());
        assertEquals(commonUserId, entity.getUserId());
    }

    @Test
    void testCreateNotificationNoUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationEntity entity = factory.manufacturePojo(NotificationEntity.class);
            entity.setUserId(null); // Validamos que el ID no sea nulo
            notificationService.createNotification(entity);
        });
    }

    @Test
    void testCreateNotificationEmptyMessage() {
        assertThrows(IllegalArgumentException.class, () -> {
            NotificationEntity entity = factory.manufacturePojo(NotificationEntity.class);
            entity.setUserId(commonUserId);
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
    }

    @Test
    void testUpdateNotification() {
        NotificationEntity entity = notificationList.get(0);
        NotificationEntity updateData = new NotificationEntity();
        updateData.setIsRead(true);
        updateData.setMessage("Mensaje Actualizado");
        updateData.setUserType("SHELTER");
        updateData.setNotificationType("ALERT");

        NotificationEntity result = notificationService.updateNotification(entity.getId(), updateData);
        
        assertNotNull(result);
        NotificationEntity updated = entityManager.find(NotificationEntity.class, entity.getId());
        assertTrue(updated.getIsRead());
        assertEquals("Mensaje Actualizado", updated.getMessage());
    }

    @Test
    void testDeleteNotificationYoungerThan30Days() {
        NotificationEntity recent = factory.manufacturePojo(NotificationEntity.class);
        recent.setUserId(commonUserId);
        recent.setTimestamp(LocalDateTime.now()); // Muy joven
        entityManager.persist(recent);
        entityManager.flush();

        assertThrows(IllegalStateException.class, () -> {
            notificationService.deleteNotification(recent.getId());
        });
    }

    @Test
    void testDeleteNotificationSuccess() {
        NotificationEntity oldNotification = notificationList.get(0);
        
        notificationService.deleteNotification(oldNotification.getId());
        entityManager.flush();

        NotificationEntity deleted = entityManager.find(NotificationEntity.class, oldNotification.getId());
        assertNull(deleted);
    }
}