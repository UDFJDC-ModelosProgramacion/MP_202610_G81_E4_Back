package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialAdopcionRepository extends JpaRepository<AdoptionHistoryEntity, Integer> {

    List<AdoptionHistoryEntity> findByAdoption(AdoptionEntity adoption);

    List<AdoptionHistoryEntity> findByReason(String reason);
}