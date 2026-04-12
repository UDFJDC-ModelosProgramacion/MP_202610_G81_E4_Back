package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
// IMPORTANTE: Agregar este import para la exclusión de Podam
import uk.co.jemos.podam.common.PodamExclude; 

import java.time.LocalDate;

@Entity
@Table(name = "adoption_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionRequestEntity extends BaseEntity {
    
    private LocalDate requestDate;
    private String status;
    private String motivation;

    @PodamExclude 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    @JsonIgnore 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AdopterEntity adopter;

    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonIgnore 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PetEntity pet;

    @PodamExclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id") 
    @JsonIgnore 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private VeterinarianEntity veterinarian; 
}