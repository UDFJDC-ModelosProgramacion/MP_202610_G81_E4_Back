package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AdoptionHistoryRepository extends JpaRepository<AdoptionHistoryEntity, Long> {
    
}