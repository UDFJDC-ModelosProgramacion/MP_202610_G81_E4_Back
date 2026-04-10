package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "trial_stays")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class TrialStayEntity extends BaseEntity {

    private LocalDate startDate;
    private LocalDate endDate;
    private String result;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonIgnoreProperties({"trialStays", "shelter"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PetEntity pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    @JsonIgnoreProperties({"trialStay"})
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AdoptionEntity adoption;
}

    
    

