package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Entity
@Table(name = "ADOPTION_TRACKING_ENTITY")
@Data
public class AdoptionTrackingEntity extends BaseEntity {
    private int trackingId;
    private int adoptionId;
    private String frequency;
    private String notes;
    private Date nextReview;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSeguimiento;
/* 
    @ManyToOne
    @JoinColumn(name = "idAdopcion", nullable = false)
    private Adopcion adopcion;

    private String frecuencia;

    private String notas;*/

    @Temporal(TemporalType.DATE)
    private Date proximaRevision;

}