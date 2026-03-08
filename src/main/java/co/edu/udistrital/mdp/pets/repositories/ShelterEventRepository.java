package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShelterEventRepository extends JpaRepository<ShelterEventEntity, Long> {
    
    // Eventos de un refugio específico
    List<ShelterEventEntity> findByShelter(ShelterEntity shelter);
    
    // Eventos futuros
    List<ShelterEventEntity> findByEventDateAfter(LocalDateTime now);
    
    // Consulta para encontrar eventos que aún tienen cupo disponible
    @Query("SELECT e FROM ShelterEventEntity e WHERE e.registeredCount < e.maxCapacity")
    List<ShelterEventEntity> findEventsWithAvailableCapacity();
}
