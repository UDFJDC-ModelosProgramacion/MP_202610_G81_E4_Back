package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

import java.time.LocalDate;
import  java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "AdoptionEntity")
@Data

public class AdoptionEntity extends BaseEntity {
    private int adoption;
    private int pet;
    private int adopter;
    private int veterinarian;
    private LocalDate adoptionDate;
    private String status;
    private boolean trialPeriod; 

    @ManyToOne
    @JoinColumn(name = "TrialStay_id")
    @PodamExclude 
    private TrialStayEntity trialStay;

    @ManyToOne
    @JoinColumn (name = "Review_id")
    @PodamExclude
    private ReviewEntity review;

    

    
    
}
