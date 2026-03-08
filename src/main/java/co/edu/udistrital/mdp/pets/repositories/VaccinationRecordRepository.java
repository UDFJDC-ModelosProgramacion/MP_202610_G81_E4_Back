package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;


public interface VaccinationRecordRepository extends JpaRepository<VaccinationRecordEntity, Long> {

    
} 
