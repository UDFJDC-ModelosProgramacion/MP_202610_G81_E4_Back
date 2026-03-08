package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevolucionRepository extends JpaRepository<DevolutionEntity, Integer> {

    List<DevolutionEntity> findByAdoption(AdoptionEntity adoption);

    List<DevolutionEntity> findByReason(String reason);
}