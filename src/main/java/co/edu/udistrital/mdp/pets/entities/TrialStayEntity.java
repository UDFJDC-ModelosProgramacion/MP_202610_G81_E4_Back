package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDate;

@Entity
@Table(name = "trial_stays")
@Data
@EqualsAndHashCode(callSuper = true)
public class TrialStayEntity extends BaseEntity {
    
    private LocalDate startDate;
    private LocalDate endDate;
    private String result;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;
}


    
    

