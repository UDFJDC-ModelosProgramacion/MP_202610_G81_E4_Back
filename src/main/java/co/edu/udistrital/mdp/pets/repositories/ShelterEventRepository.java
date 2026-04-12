package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterEventRepository extends JpaRepository<ShelterEventEntity, Long> {
    Optional<ShelterEventEntity> findByEventCode(Long eventCode);
    
}
