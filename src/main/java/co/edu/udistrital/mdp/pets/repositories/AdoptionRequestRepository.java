package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import java.util.List;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequestEntity, Long> {

}
