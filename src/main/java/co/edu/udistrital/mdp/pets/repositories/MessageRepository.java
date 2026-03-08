package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    List<Message> findBySenderId(Long senderId);
    
    List<Message> findByRecipientId(Long recipientId);
    
    List<Message> findByRecipientIdAndIsRead(Long recipientId, Boolean isRead);
    
    List<Message> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    
    List<Message> findByRecipientIdOrderByTimestampDesc(Long recipientId);
    
    long countByRecipientIdAndIsRead(Long recipientId, Boolean isRead);
}