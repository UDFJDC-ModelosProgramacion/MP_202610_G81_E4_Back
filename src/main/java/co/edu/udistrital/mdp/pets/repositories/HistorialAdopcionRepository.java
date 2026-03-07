package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.HistorialAdopcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialAdopcionRepository extends JpaRepository<HistorialAdopcion, Integer> {

    List<HistorialAdopcion> findByIdMascota(int idMascota);

    List<HistorialAdopcion> findByTipo(String tipo);
}