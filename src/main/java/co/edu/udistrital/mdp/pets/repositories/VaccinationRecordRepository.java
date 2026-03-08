package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecordEntity, Long> {
    
    Optional<VaccinationRecordEntity> findByPet(PetEntity pet);
}