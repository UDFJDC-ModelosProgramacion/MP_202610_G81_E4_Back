package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "vaccination_records")
@Data
@EqualsAndHashCode(callSuper = true)
public class VaccinationRecordEntity extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ToString.Exclude
    @OneToMany(mappedBy = "vaccinationRecord", cascade = CascadeType.ALL)
    private List<VaccineEntity> vaccines;
}