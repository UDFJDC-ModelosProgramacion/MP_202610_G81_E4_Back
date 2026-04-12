package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude; 

import java.util.List;

@Entity
@Table(name = "vaccination_records")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationRecordEntity extends BaseEntity {

    @PodamExclude 
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @PodamExclude
    @ToString.Exclude
    @OneToMany(mappedBy = "vaccinationRecord", cascade = CascadeType.ALL)
    private List<VaccineEntity> vaccines;
}