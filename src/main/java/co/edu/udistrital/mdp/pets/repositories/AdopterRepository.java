package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AdopterRepository extends JpaRepository<AdopterEntity, Long> {
    
    Optional<AdopterEntity> findByEmail(String email);
    
    // Búsqueda por el ID de negocio (Cédula/Documento)
    Optional<AdopterEntity> findByAdopterIdBusiness(Long adopterIdBusiness);
    
    List<AdopterEntity> findByHousingType(String housingType);
    
    List<AdopterEntity> findByHasChildren(Boolean hasChildren);
    
    boolean existsByEmail(String email);
}