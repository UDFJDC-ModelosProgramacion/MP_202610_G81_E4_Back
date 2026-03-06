package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Table(name = "ShelterEntity")
@Data
public class ShelterEntity extends BaseEntity {
    private int shelter;
    private String name;
    private String city;
    private String address;
    private String phone;
    private String email;
    private String[] photos;
    private String[] videos;
    private String description; 

    @ManyToOne
    @JoinColumn(name = "ShelterEvent_id")
    @PodamExclude 
    private ShelterEventEntity shelterEvent;

    @ManyToOne
    @JoinColumn (name = "Adoption_id")
    @PodamExclude
    private AdoptionEntity adoption; 


    
}
