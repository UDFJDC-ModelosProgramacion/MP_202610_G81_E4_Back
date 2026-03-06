package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adopters")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Adopter extends User {
    
    @Column(name = "adopter_id", unique = true)
    private Integer adopterId;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @Column(length = 255)
    private String address;
    
    @Column(name = "housing_type", length = 50)
    private String housingType;
    
    @Column(name = "has_children")
    private Boolean hasChildren;
    
    @Column(name = "has_other_pets")
    private Boolean hasOtherPets;
    
    @ElementCollection
    @CollectionTable(name = "adopter_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "preference")
    private List<String> preferences = new ArrayList<>();
    
    public void addPreference(String preference) {
        if (this.preferences == null) {
            this.preferences = new ArrayList<>();
        }
        this.preferences.add(preference);
    }
    
    public void requestAdoption(Integer petId) {
        System.out.println("Adopter " + getLastName() + " has requested to adopt pet with ID: " + petId);
    }
    
    public void startTrialCoexistence(Integer petId) {
        System.out.println("Starting trial coexistence with pet ID: " + petId);
    }
    
    public void confirmAdoption(Integer petId) {
        System.out.println("Adoption confirmed for pet ID: " + petId);
    }
    
    public void returnPet(Integer petId) {
        System.out.println("Returning pet ID: " + petId);
    }
    
    public void updatePetInfo(Integer petId) {
        System.out.println("Updating information for pet ID: " + petId);
    }
    
    public void leaveReview(Integer petId, String comment) {
        System.out.println("Review left for pet ID: " + petId);
    }
}