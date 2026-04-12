package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
// No olvides el import mágico
import uk.co.jemos.podam.common.PodamExclude; 
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "veterinarians")
@Getter
@Setter
public class VeterinarianEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private Long veterinarianIdBusiness;

    private String lastName;

    private String availability;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"))
    @Column(name = "specialty")
    private List<String> specialties = new ArrayList<>();

    @PodamExclude 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private ShelterEntity shelter;

    @PodamExclude 
    @OneToMany(mappedBy = "veterinarian", cascade = CascadeType.ALL)
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "veterinarian")
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();
}