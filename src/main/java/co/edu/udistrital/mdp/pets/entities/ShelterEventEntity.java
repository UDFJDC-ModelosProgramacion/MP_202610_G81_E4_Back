package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import  java.time.LocalDateTime;
import java.util.Date;

public class ShelterEventEntity extends BaseEntity {
    private int event;
    private int shelter;
    private String eventType;
    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private int maxCapacity;
    private int registeredCount;

    
}
