package co.edu.udistrital.mdp.pets.repositories;
import co.edu.udistrital.mdp.pets.entities.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {

    List<Devolucion> findByIdAdopcion(int idAdopcion);

    List<Devolucion> findByIdMascota(int idMascota);

    List<Devolucion> findByMotivo(String motivo);
}