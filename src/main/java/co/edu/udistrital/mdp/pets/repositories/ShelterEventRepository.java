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
    
}
