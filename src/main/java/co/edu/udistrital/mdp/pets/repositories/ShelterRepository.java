package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;

@Repository
public interface ShelterRepository extends JpaRepository<ShelterEntity, Long> {
    
    // Buscar refugios por ciudad
    List<ShelterEntity> findByCityIgnoreCase(String city);
    
    // Buscar por nombre exacto
    Optional<ShelterEntity> findByName(String name);
    
    // Verificar si existe un refugio por email
    boolean existsByEmail(String email);
}