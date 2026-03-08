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
    private Long veterinarianId;
    
    @Column(nullable = false, length = 100)
    private String lastName;
    
    @ElementCollection
    @CollectionTable(name = "veterinarian_specialties", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();
    
    @Column(length = 100)
    private String availability;
    
    @Column(name = "shelter_id", insertable = false, updatable = false)
    private Long shelterId;
    
    // ==================== RELACIONES ====================
    
    // Relación con Refugio (Grupo 3)
    // Descomenta cuando exista la entidad Shelter
    /*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private Shelter shelter;
    */
    
    // Relación con Adopcion (Grupo 3)
    // Descomenta cuando exista la entidad Adoption
    /*
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<Adoption> adoptions = new ArrayList<>();
    */
    
    // Relación con EventoMedico (Grupo 2)
    // Descomenta cuando exista la entidad MedicalEvent
    /*
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<MedicalEvent> medicalEvents = new ArrayList<>();
    */
    
    // Relación con Vacuna (Grupo 2)
    // Descomenta cuando exista la entidad Vaccine
    /*
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<Vaccine> vaccines = new ArrayList<>();
    */
    
    // Relación con SeguimientoAdopcion (Grupo 4)
    // Descomenta cuando exista la entidad AdoptionFollowUp
    /*
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<AdoptionFollowUp> adoptionFollowUps = new ArrayList<>();
    */
}