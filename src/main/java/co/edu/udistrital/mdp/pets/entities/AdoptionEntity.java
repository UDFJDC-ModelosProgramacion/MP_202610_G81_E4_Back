package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import  java.time.LocalDateTime;
import java.util.Date;

public class AdoptionEntity extends BaseEntity {
    private int adoption;
    private int pet;
    private int adopter;
    private int veterinarian;
    private Date adoptionDate;
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
