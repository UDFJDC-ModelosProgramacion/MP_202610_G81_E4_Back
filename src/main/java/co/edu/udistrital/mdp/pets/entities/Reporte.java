package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "reporte")
@Data
public class Reporte extends BaseEntity {

    private String tipoReporte;
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    private String datos;
    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

    @ManyToOne
    @JoinColumn(name = "Shelter_id")
    private ShelterEntity shelter;
}