package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "historial_adopcion")
public class HistorialAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHistorial;

    @ManyToOne
    @JoinColumn(name = "idMascota", nullable = false)
    private Mascota mascota;

    private String tipo;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private String motivo;

    private String detalles;

}