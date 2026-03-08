package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.SeguimientoAdopcion;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoAdopcionRepository extends JpaRepository<SeguimientoAdopcion, Integer> {

    List<SeguimientoAdopcion> findByAdoption(AdoptionEntity adoption);

    List<SeguimientoAdopcion> findByFrecuencia(String frecuencia);
}