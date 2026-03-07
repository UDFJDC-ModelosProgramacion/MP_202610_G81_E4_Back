package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Entity
@Table(name = "ADOPTION_HISTORY_ENTITY")
@Data
public class AdoptionHistoryEntity extends BaseEntity{
    
    @Temporal(TemporalType.DATE)
    private Date date;

    private String reason;

    private String detail;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Historial;
/* 
    @ManyToOne
    @JoinColumn(name = "idMascota", nullable = false)
    private Mascota mascota;
    private String tipo;
*/
}