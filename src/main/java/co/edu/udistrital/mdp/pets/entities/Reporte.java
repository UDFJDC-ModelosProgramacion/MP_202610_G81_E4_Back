package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReporte;

    @ManyToOne
    @JoinColumn(name = "idRefugio", nullable = false)
    private Refugio refugio;

    private String tipoReporte;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    private String datos;

    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

}