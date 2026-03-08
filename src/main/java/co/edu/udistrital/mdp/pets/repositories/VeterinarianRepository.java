package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
    
    Optional<Veterinarian> findByEmail(String email);
    
    Optional<Veterinarian> findByVeterinarianId(Long veterinarianId);
    
    List<Veterinarian> findByShelterId(Long shelterId);
    
    List<Veterinarian> findByAvailability(String availability);
    
    boolean existsByEmail(String email);
}