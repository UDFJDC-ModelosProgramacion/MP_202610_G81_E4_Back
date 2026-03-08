package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepository extends JpaRepository<VaccineEntity, Long> {

    List<VaccineEntity> findByApplicationDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT v FROM VaccineEntity v WHERE v.nextApplicationDate <= :reminderDate")
    List<VaccineEntity> findUpcomingVaccinations(@Param("reminderDate") LocalDate reminderDate);
}
