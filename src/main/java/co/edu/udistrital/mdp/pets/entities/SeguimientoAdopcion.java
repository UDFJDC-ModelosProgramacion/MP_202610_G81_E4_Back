package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "seguimiento_adopcion")
@Data
public class SeguimientoAdopcion extends BaseEntity {

    private String frecuencia;
    private String notas;
    @Temporal(TemporalType.DATE)
    private Date proximaRevision;

    @ManyToOne
    @JoinColumn(name = "Adoption_id")
    private AdoptionEntity adoption;
}