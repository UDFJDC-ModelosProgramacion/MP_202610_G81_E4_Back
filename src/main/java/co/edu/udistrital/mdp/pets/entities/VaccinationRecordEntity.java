package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Data
@Entity
public class VaccinationRecordEntity extends BaseEntity{
    @ManyToOne
    private PetEntity pet;

    
    @OneToMany(mappedBy = "vaccinationRecord")
    private List<VaccineEntity> vaccines;

}