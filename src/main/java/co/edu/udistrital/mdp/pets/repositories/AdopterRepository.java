package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AdopterRepository extends JpaRepository<AdopterEntity, Long> {
    
    Optional<AdopterEntity> findByEmail(String email);
    
    Optional<AdopterEntity> findByAdopterId(Long adopterId);
    
    List<AdopterEntity> findByHousingType(String housingType);
    
    List<AdopterEntity> findByHasChildren(Boolean hasChildren);
    
    List<AdopterEntity> findByHasOtherPets(Boolean hasOtherPets);
    
    boolean existsByEmail(String email);
}