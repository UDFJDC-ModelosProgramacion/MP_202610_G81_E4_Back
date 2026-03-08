package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VeterinarianRepository extends JpaRepository<VeterinarianEntity, Long> {
    
    Optional<VeterinarianEntity> findByEmail(String email);
    
    List<VeterinarianEntity> findByShelterIdAndAvailability(Long shelterId, String availability);
    
    List<VeterinarianEntity> findBySpecialtiesContaining(String specialty);
}