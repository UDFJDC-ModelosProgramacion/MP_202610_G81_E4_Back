package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PetDTO {

    private Long id;
    private String name;
    private String species;
    private String breed;
    private int age;
    private String sex;
    private String size;
    private String temperament;
    private String specialNeeds;
    private String arrivalHistory;
    private String status;
    private Long shelterId;

    public PetDTO(PetEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.species = entity.getSpecies();
            this.breed = entity.getBreed();
            this.age = entity.getAge();
            this.sex = entity.getSex();
            this.size = entity.getSize();
            this.temperament = entity.getTemperament();
            this.specialNeeds = entity.getSpecialNeeds();
            this.arrivalHistory = entity.getArrivalHistory();
            this.status = entity.getStatus();
            
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
            }
        }
    }
}