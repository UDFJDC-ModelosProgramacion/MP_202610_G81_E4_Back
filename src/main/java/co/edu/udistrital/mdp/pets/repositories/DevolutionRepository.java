package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DevolutionRepository extends JpaRepository<DevolutionEntity, Long> {
    List<DevolutionEntity> findByAdoption(AdoptionEntity adoption);
}