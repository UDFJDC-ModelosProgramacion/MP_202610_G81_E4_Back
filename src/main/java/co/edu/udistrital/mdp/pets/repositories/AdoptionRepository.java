package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;


public interface AdoptionRepository extends JpaRepository<AdoptionEntity, Long> {
    
}