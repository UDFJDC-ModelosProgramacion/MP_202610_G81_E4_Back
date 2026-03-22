package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

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
    private List<String> specialties;

    @Column(length = 100)
    private String availability;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;

    @ToString.Exclude
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<AdoptionEntity> adoptions;

    @ToString.Exclude
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<MedicalEventEntity> medicalEvents;
}