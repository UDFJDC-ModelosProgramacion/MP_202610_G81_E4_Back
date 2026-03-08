package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import  java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "adoptions")
@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptionEntity extends BaseEntity {
    
    private LocalDate adoptionDate;
    private String status;
    private boolean trialPeriod; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

    @OneToOne(fetch = FetchType.LAZY) // Generalmente es 1 a 1
    @JoinColumn(name = "trial_stay_id")
    @PodamExclude 
    private TrialStayEntity trialStay;

    @OneToOne(mappedBy = "adoption", cascade = CascadeType.ALL)
    @PodamExclude
    private ReviewEntity review;
}
    
