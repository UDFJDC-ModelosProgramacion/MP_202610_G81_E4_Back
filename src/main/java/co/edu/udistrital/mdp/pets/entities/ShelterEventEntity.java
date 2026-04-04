package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDateTime;

@Entity
@Table(name = "shelter_events") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "shelter") 
@ToString(exclude = "shelter") 
public class ShelterEventEntity extends BaseEntity {
    
    @Column(name = "event_code", unique = true)
    private Integer eventCode;

    @Column(name = "event_type")
    private String eventType;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    
    private String location;
    
    @Column(name = "max_capacity")
    private int maxCapacity;
    
    @Column(name = "registered_count")
    private int registeredCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    @PodamExclude 
    private ShelterEntity shelter;
}

