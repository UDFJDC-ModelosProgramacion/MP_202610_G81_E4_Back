package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@Table(name = "adoptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnoreProperties("adoptions") // Ignora la lista dentro de Pet
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @JsonIgnoreProperties("adoptions") // Ignora la lista dentro de Adopter
    private AdopterEntity adopter;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
    @JoinColumn(name = "trial_stay_id")
    private TrialStayEntity trialStay;

    @OneToOne(mappedBy = "adoption", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("adoption")
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veterinarian_id")
    @JsonIgnoreProperties("adoptions")
    private VeterinarianEntity veterinarian;
}
    
