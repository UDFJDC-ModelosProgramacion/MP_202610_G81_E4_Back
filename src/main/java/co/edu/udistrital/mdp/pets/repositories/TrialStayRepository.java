package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrialStayRepository extends JpaRepository<TrialStayEntity, Long> {
    List<TrialStayEntity> findByPetId(Long petId);

    List<TrialStayEntity> findByResult(String result);

}