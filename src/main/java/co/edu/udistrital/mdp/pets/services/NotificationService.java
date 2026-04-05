package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.entities.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(Notification n) {

        if (n.getUserId() == null) {
            throw new IllegalArgumentException("User required");
        }

        if (n.getMessage() == null || n.getMessage().isEmpty()) {
            throw new IllegalArgumentException("Message required");
        }

        String[] validTypes = {
                "MESSAGE",
                "ADOPTION",
                "SYSTEM"
        };

        boolean valid = false;

        for (String t : validTypes) {
            if (t.equals(n.getType())) {
                valid = true;
            }
        }

        if (!valid) {
            throw new IllegalArgumentException("Invalid type");
        }

        n.setTimestamp(LocalDateTime.now());
        n.setRead(false);

        return notificationRepository.save(n);
    }


    public Notification updateNotification(Long id, Notification updated) {

        Notification existing =
                notificationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Not found"));


        existing.setRead(updated.getRead());

        return notificationRepository.save(existing);
    }

    public void deleteNotification(Long id, Long userId) {

        Notification n =
                notificationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Not found"));


        if (!n.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Not owner");
        }

        long days =
                ChronoUnit.DAYS.between(
                        n.getTimestamp(),
                        LocalDateTime.now()
                );

        if (days < 30) {
            throw new IllegalArgumentException(
                    "Must be older than 30 days"
            );
        }

        notificationRepository.deleteById(id);
    }
}