package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Entity
@Table(name = "shelter_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "shelter")
public class ShelterEventEntity extends BaseEntity {

    /**
     * Código de negocio para el evento. 
     * Se genera manualmente en el Service para evitar conflictos con el ID de BaseEntity.
     */
    @Column(name = "event_code", unique = true)
    private Long eventCode;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "location")
    private String location;

    @Column(name = "max_capacity")
    private int maxCapacity;

    @Column(name = "registered_count")
    private int registeredCount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shelter_id")
    @JsonIgnoreProperties("events") 
    private ShelterEntity shelter;
}