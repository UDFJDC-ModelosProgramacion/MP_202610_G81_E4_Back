package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veterinarians")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Veterinarian extends User {
    
    @Column(name = "veterinarian_id", unique = true)
    private Integer veterinarianId;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @ElementCollection
    @CollectionTable(name = "veterinarian_specialties", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();
    
    @Column(length = 100)
    private String availability;
    
    @Column(name = "shelter_id")
    private Integer shelterId;
    
    public void addSpecialty(String specialty) {
        if (this.specialties == null) {
            this.specialties = new ArrayList<>();
        }
        this.specialties.add(specialty);
    }
    
    public void registerMedicalEvent(Integer petId, String event) {
        log.info("Medical event registered for pet ID: {}", petId);
    }
    
    public void applyVaccine(Integer petId, String vaccine) {
        log.info("Vaccine {} applied to pet ID: {}", vaccine, petId);
    }
    
    public void performFollowUp(Integer adoptionId) {
        log.info("Performing follow-up for adoption ID: {}", adoptionId);
    }
    
    public void updateVaccinationRecord(Integer petId) {
        log.info("Vaccination record updated for pet ID: {}", petId);
    }
    
    public void scheduleCheckup(Integer adoptionId) {
        log.info("Checkup scheduled for adoption ID: {}", adoptionId);
    }
    
    public void viewAssignedPets() {
        log.info("Showing pets assigned to veterinarian {}", getName());
    }
}