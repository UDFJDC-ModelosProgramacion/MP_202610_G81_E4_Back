package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pets")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"vaccinationRecords", "shelter"})
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity extends BaseEntity {

    private String name;
    private String species;
    private String breed;
    private int age;
    private String sex;
    private String size;
    private String temperament;

    @Column(columnDefinition = "TEXT")
    private String specialNeeds;

    @ElementCollection
    private List<String> photos;

    @Column(columnDefinition = "TEXT")
    private String arrivalHistory;

    private LocalDate arrivalDate;

    private String status;

    @ToString.Exclude
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pet")
    private List<VaccinationRecordEntity> vaccinationRecords;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonIgnoreProperties("pets")
    private ShelterEntity shelter;
}