package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.MedicalEvent;

public interface MedicalEventRepository extends JpaRepository<MedicalEvent, Long> {

    
} 
