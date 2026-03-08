package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    
    Optional<Adopter> findByEmail(String email);
    
    Optional<Adopter> findByAdopterId(Long adopterId);
    
    List<Adopter> findByHousingType(String housingType);
    
    List<Adopter> findByHasChildren(Boolean hasChildren);
    
    List<Adopter> findByHasOtherPets(Boolean hasOtherPets);
    
    boolean existsByEmail(String email);
}