package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class MedicalEvent extends BaseEntity{
    private String eventType;

    private LocalDate eventDate;

    private String description;

    private String diagnosis;

    private String treatment;

    @ElementCollection
    private List<String> attachments;

    @ManyToOne
    private PetEntity pet;
}