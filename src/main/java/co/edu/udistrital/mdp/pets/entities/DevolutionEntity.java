package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "devolutions")
@Data
@EqualsAndHashCode(callSuper = true)
public class DevolutionEntity extends BaseEntity {

    private LocalDate returnDate;
    private String reason;
    
    @Column(columnDefinition = "TEXT")
    private String detailedDescription;
    
    private String petState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;
}