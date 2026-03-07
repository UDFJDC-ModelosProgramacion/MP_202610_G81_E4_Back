package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Data
@Entity
public class VaccinationRecord extends BaseEntity{
    @ManyToOne
    private Pet pet;

    @OneToMany(mappedBy = "vaccinationRecord")
    private List<Vaccine> vaccines;

}