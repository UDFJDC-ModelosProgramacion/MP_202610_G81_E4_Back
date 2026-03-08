package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.Vaccine;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    
} 
