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
    private Long adopterId;
    
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
    
    // ==================== RELACIONES ====================
    
    // Relación con SolicitudAdopcion (Grupo 2)
    // Descomenta cuando exista la entidad AdoptionRequest
    /*
    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL)
    private List<AdoptionRequest> adoptionRequests = new ArrayList<>();
    */
    
    // Relación con Adopcion (Grupo 3)
    // Descomenta cuando exista la entidad Adoption
    /*
    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL)
    private List<Adoption> adoptions = new ArrayList<>();
    */
    
    // Relación con Reseña (Grupo 3)
    // Descomenta cuando exista la entidad Review
    /*
    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();
    */
    
    // Relación con Mensaje como remitente/destinatario
    // Ya manejado por senderId/recipientId en Message
}