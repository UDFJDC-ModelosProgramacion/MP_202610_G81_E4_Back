package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import  java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "TRIAL_STAY_ENTITY")
@Data
public class TrialStayEntity extends BaseEntity {
    private int trial;
    private int adoption;
    private Date startDate;
    private Date endDate;
    private int durationDays;
    private String result;
    private String observations;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;

    
    
}
