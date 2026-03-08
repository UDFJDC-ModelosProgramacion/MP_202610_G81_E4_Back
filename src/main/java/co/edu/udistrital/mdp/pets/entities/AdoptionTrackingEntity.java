package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "ADOPTION_TRACKING_ENTITY")
@Data
public class AdoptionTrackingEntity extends BaseEntity {

    private String frequency;
    private String notes;
    @Temporal(TemporalType.DATE)
    private Date nextReview;

    @ManyToOne
    @JoinColumn(name = "Adoption_id")
    private AdoptionEntity adoption;
}