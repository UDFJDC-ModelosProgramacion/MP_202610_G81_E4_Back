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
@EqualsAndHashCode(callSuper = true, exclude = {"pet", "adopter", "trialStay", "review", "veterinarian"})
public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnoreProperties({"adoptions", "vaccinationRecords", "shelter"}) 
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @JsonIgnoreProperties("adoptions")
    private AdopterEntity adopter;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
    @JoinColumn(name = "trial_stay_id")
    @JsonIgnoreProperties("adoption")
    private TrialStayEntity trialStay;

    @OneToOne(mappedBy = "adoption", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("adoption")
    private ReviewEntity review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veterinarian_id")
    @JsonIgnoreProperties({"adoptions", "appointments"})
    private VeterinarianEntity veterinarian;
}
    
