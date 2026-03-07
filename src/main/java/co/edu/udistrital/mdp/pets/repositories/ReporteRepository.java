package co.edu.udistrital.mdp.pets.repositories;
import co.edu.udistrital.mdp.pets.entities.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Integer> {

    List<Reporte> findByIdRefugio(int idRefugio);

    List<Reporte> findByTipoReporte(String tipoReporte);
}