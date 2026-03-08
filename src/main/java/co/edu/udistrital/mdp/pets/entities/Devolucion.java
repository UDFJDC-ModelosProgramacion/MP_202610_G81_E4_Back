package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "devolucion")
@Data
public class Devolucion extends BaseEntity {

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    private String motivo;
    private String descripcionDetallada;
    private String estadoMascota;

    @ManyToOne
    @JoinColumn(name = "Adoption_id")
    private AdoptionEntity adoption;
}