package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "DEVOLUTION_ENTITY")
@Data
public class DevolutionEntity extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date returnDate;
    private String reason;
    private String detailedDescription;
    private String petState;

    @ManyToOne
    @JoinColumn(name = "Adoption_id")
    private AdoptionEntity adoption;
}