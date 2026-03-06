package co.edu.udistrital.mdp.pets.entities;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class Vaccine extends BaseEntity {
    private String vaccineName;

    private LocalDate applicationDate;

    private LocalDate nextApplicationDate;

    private String batchNumber;

    private String observations;

    @ManyToOne
    private VaccinationRecord vaccinationRecord;
}
