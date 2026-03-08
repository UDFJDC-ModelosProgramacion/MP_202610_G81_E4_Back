package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;


public interface TrialStayRepository extends JpaRepository<TrialStayEntity, Long> {
    
}
