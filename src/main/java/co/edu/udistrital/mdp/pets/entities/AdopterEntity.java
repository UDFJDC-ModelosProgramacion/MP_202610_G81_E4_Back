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
public class AdopterEntity extends UserEntity {

    // adopterId como identificador de negocio (ej. cédula), no como Primary Key
    @Column(name = "adopter_id_business", unique = true)
    private Long adopterIdBusiness;
    
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
    @CollectionTable(name = "adopter_preferences", joinColumns = @JoinColumn(name = "adopter_id"))
    @Column(name = "preference")
    private List<String> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();
}