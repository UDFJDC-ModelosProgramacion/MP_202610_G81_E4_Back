package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "adopters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    private String address;

    @Column(name = "housing_type")
    private String housingType;

    private Boolean hasChildren;
    private Boolean hasOtherPets;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "adopter_preferences", joinColumns = @JoinColumn(name = "adopter_id"))
    @Column(name = "preference")
    private List<String> preferences = new ArrayList<>();

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("adopter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("adopter")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AdoptionEntity> adoptions = new ArrayList<>();
}