package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "adoption_trackings")
@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptionTrackingEntity extends BaseEntity {

    private String frequency;
    private String notes;
    private LocalDate nextReview; // Cambiado de Date a LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;
}