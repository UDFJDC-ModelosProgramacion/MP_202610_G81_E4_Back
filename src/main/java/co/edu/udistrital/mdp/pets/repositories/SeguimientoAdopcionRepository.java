
package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguimientoAdopcionRepository extends JpaRepository<AdoptionTrackingEntity, Integer> {

    List<AdoptionTrackingEntity> findByIdAdopcion(int idAdopcion);

    List<AdoptionTrackingEntity> findByFrecuencia(String frecuencia);
}