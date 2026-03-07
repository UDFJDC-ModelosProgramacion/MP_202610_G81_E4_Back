package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;

@Entity
@Table(name = "REPORT_ENTITY")
@Data
public class ReportEntity extends BaseEntity {
    private int reportId;
    private int refugeId;
    private String reportType;
    private Date startDate;
    private Date endDate;
    private String data;
    private Date generationDate;
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReporte;
/* 
    @ManyToOne
    @JoinColumn(name = "idRefugio", nullable = false)
    private Refugio refugio;*/

    private String tipoReporte;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    private String datos;

    @Temporal(TemporalType.DATE)
    private Date fechaGeneracion;

}