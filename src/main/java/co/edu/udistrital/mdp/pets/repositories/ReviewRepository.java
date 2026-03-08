package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;

public interface ReviewRepository extends  JpaRepository<ReviewEntity, Long> {
    
}
