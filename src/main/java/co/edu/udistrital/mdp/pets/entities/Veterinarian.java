package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veterinarians")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
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
        System.out.println("Medical event registered for pet ID: " + petId);
    }
    
    public void applyVaccine(Integer petId, String vaccine) {
        System.out.println("Vaccine " + vaccine + " applied to pet ID: " + petId);
    }
    
    public void performFollowUp(Integer adoptionId) {
        System.out.println("Performing follow-up for adoption ID: " + adoptionId);
    }
    
    public void updateVaccinationRecord(Integer petId) {
        System.out.println("Vaccination record updated for pet ID: " + petId);
    }
    
    public void scheduleCheckup(Integer adoptionId) {
        System.out.println("Checkup scheduled for adoption ID: " + adoptionId);
    }
    
    public void viewAssignedPets() {
        System.out.println("Showing pets assigned to veterinarian " + getLastName());
    }
}