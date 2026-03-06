package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import  java.time.LocalDateTime;
import java.util.Date;

public class TrialStayEntity extends BaseEntity {
    private int trial;
    private int adoption;
    private Date startDate;
    private Date endDate;
    private int durationDays;
    private String result;
    private String observations;

    
    
}
