package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "adoption_requests")
@EqualsAndHashCode(callSuper = true)
public class AdoptionRequestEntity extends BaseEntity {
    
    private LocalDate requestDate;
    private String status;
    private String motivation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter; // Este nombre debe ser igual al mappedBy en AdopterEntity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;
}