package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "historial_adopcion")
@Data
public class HistorialAdopcion extends BaseEntity {

    private String tipo;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    private String motivo;
    private String detalles;

    @ManyToOne
    @JoinColumn(name = "Adoption_id")
    private AdoptionEntity adoption;
}