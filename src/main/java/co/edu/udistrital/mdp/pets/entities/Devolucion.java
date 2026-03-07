package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devolucion")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDevolucion;

    @ManyToOne
    @JoinColumn(name = "idAdopcion", nullable = false)
    private Adopcion adopcion;

    @ManyToOne
    @JoinColumn(name = "idMascota", nullable = false)
    private Mascota mascota;

    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;

    private String motivo;

    private String descripcionDetallada;

    private String estadoMascota;

}