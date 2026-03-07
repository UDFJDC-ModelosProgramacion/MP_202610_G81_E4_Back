package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seguimiento_adopcion")
public class SeguimientoAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSeguimiento;

    @ManyToOne
    @JoinColumn(name = "idAdopcion", nullable = false)
    private Adopcion adopcion;

    private String frecuencia;

    private String notas;

    @Temporal(TemporalType.DATE)
    private Date proximaRevision;

}