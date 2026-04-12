package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "medical_events")
@EqualsAndHashCode(callSuper = true)
public class MedicalEventEntity extends BaseEntity {

    private String eventType;
    private LocalDate eventDate;
    private String description;
    private String diagnosis;
    private String treatment;

    @ElementCollection
    private List<String> attachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;
    
}