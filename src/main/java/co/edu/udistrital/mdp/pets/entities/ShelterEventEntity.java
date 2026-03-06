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
@Table(name = "ShelterEventEntity")
@Data
public class ShelterEventEntity extends BaseEntity {
    private int event;
    private String eventType;
    private String title;
    private String description;
    private Date eventDate;
    private String location;
    private int maxCapacity;
    private int registeredCount;

    @ManyToOne
    @JoinColumn(name = "Shelter_id")
    @PodamExclude 
    private ShelterEntity shelter;
}
