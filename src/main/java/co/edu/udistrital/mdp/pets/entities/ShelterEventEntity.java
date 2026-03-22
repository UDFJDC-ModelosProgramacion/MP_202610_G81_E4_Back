
package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDateTime;

@Entity
@Table(name = "shelter_events") 
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ShelterEventEntity extends BaseEntity {
    
    @Column(name = "event_code")
    private Integer eventCode;

    private String eventType;
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private LocalDateTime eventDate;
    private String location;
    private int maxCapacity;
    private int registeredCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    @PodamExclude 
    private ShelterEntity shelter;
}

