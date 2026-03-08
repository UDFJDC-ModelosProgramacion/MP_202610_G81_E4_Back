package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.ShelterEventEntity;

public interface ShelterEventRepository extends JpaRepository<ShelterEventEntity, Long> {
    
}