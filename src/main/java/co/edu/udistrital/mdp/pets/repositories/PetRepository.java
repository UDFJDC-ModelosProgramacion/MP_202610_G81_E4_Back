
package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {
  
    List<PetEntity> findByStatus(String status);
  
    List<PetEntity> findBySpeciesAndSex(String species, String sex);

    @Query("SELECT p FROM PetEntity p WHERE p.vaccinationRecords IS EMPTY")
    List<PetEntity> findPetsWithoutVaccinationHistory();
}