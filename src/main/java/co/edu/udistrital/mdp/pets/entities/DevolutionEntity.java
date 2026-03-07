package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "DEVOLUTION_ENTITY")
@Data
public class DevolutionEntity  extends BaseEntity {
    private int returnId;
    private int adoptionId;
    private int petId;
    private Date returnDate;
    private String reason;
    private String detailedDescription;

    @ManyToOne
    @JoinColumn(name = "idAdopcion", nullable = false)
    private AdoptionHistoryEntity adoptionHistoryEntity;

    // @ManyToOne
    // @JoinColumn(name = "idMascota", nullable = false)
    // private Pet pet;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;


    private String deatiledDescription;

    private String petState;

}