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
public class VeterinarianEntity extends UserEntity {
    
    @Column(name = "veterinarian_id_business", unique = true)
    private Long veterinarianIdBusiness; 
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @ElementCollection
    @CollectionTable(name = "veterinarian_specialties", joinColumns = @JoinColumn(name = "veterinarian_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();
    
    @Column(length = 100)
    private String availability;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;
    
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<AdoptionEntity> adoptions = new ArrayList<>();
    
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();
}
