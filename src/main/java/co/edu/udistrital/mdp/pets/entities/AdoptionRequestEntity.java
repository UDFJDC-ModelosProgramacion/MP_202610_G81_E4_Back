package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    @JsonIgnore 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private AdopterEntity adopter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonIgnore 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PetEntity pet;
}