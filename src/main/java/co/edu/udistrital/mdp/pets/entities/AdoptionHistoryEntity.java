package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Table(name = "adoption_histories") 
@Data
@EqualsAndHashCode(callSuper = true)
public class AdoptionHistoryEntity extends BaseEntity {

    private LocalDate date;
    private String reason;
    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;
}