package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
@Data
@EqualsAndHashCode(callSuper = true)
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
    private List<String> photos = new ArrayList<>();
    
    @Column(columnDefinition = "TEXT")
    private String arrivalHistory;

    private LocalDate arrivalDate;
    private String status;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<VaccinationRecordEntity> vaccinationRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;
}