package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "seguimiento_adopcion")
public class SeguimientoAdopcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSeguimiento;
    private int idAdopcion;
    private String frecuencia;
    private String notas;
    @Temporal(TemporalType.DATE)
    private Date proximaRevision;

    public SeguimientoAdopcion() {}

    public int getIdSeguimiento() { return idSeguimiento; }
    public void setIdSeguimiento(int idSeguimiento) { this.idSeguimiento = idSeguimiento; }
    public int getIdAdopcion() { return idAdopcion; }
    public void setIdAdopcion(int idAdopcion) { this.idAdopcion = idAdopcion; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public Date getProximaRevision() { return proximaRevision; }
    public void setProximaRevision(Date proximaRevision) { this.proximaRevision = proximaRevision; }
}