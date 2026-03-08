package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity

public class PetEntity extends BaseEntity{
    private String name;
    private String species;
    private String breed;
    private int age;
    private String sex;
    private String size;
    private String temperament;
    private String specialNeeds;

    @ElementCollection
    private List<String> photos;
    private String arrivalHistory;

    private LocalDate arrivalDate;

    private String status;
    @OneToMany(mappedBy = "pet")
    private List<VaccinationRecordEntity> vaccinationRecords;
}
