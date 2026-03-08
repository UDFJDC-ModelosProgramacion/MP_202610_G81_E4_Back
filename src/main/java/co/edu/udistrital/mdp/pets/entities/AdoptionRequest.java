package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class AdoptionRequest extends BaseEntity{
    private LocalDate requestDate;

    private String status;

    private String motivation;

    @ElementCollection
    private List<String> documents;

    @ManyToOne
    private PetEntity pet;
    
}