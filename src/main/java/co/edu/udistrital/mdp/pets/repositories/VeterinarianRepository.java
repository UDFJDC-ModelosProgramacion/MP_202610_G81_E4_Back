package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface VeterinarianRepository extends JpaRepository<VeterinarianEntity, Long> {
    
    Optional<VeterinarianEntity> findByEmail(String email);
    
    Optional<VeterinarianEntity> findByVeterinarianId(Long veterinarianId);
    
    List<VeterinarianEntity> findByShelterId(Long shelterId);
    
    List<VeterinarianEntity> findByAvailability(String availability);
    
    boolean existsByEmail(String email);
}