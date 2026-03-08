package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DevolutionRepository extends JpaRepository<DevolutionEntity, Long> {
    
    List<DevolutionEntity> findByAdoption(AdoptionEntity adoption);

    List<DevolutionEntity> findByReasonContainingIgnoreCase(String reason);


    List<DevolutionEntity> findByReturnDate(LocalDate returnDate);

    List<DevolutionEntity> findByPetState(String petState);

    List<DevolutionEntity> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);
}