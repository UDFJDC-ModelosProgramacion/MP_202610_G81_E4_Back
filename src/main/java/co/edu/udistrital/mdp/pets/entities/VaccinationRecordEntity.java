package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vaccination_records")
@Data
@EqualsAndHashCode(callSuper = true)
public class VaccinationRecordEntity extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @OneToMany(mappedBy = "vaccinationRecord", cascade = CascadeType.ALL)
    private List<VaccineEntity> vaccines = new ArrayList<>();
}