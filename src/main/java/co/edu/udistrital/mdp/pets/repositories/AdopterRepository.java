package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.Adopter;

@Repository
public interface AdopterRepository extends JpaRepository<Adopter, Long> {
    
    Optional<Adopter> findByEmail(String email);
    
    Optional<Adopter> findByAdopterId(Long adopterId);
    
    List<Adopter> findByHousingType(String housingType);
    
    List<Adopter> findByHasChildren(Boolean hasChildren);
    
    List<Adopter> findByHasOtherPets(Boolean hasOtherPets);
    
    boolean existsByEmail(String email);
}