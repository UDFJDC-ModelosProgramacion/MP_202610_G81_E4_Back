package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    
    // Buscar reseñas de un adoptante específico
    List<ReviewEntity> findByAdopter(AdopterEntity adopter);
    
    // Buscar reseñas con calificación alta (ej. 4 o 5 estrellas)
    List<ReviewEntity> findByRatingGreaterThanEqual(Integer rating);
    
    // Obtener la reseña asociada a una adopción específica
    List<ReviewEntity> findByAdoptionId(Long adoptionId);
}
