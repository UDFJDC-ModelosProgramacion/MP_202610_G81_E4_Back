package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "devolucion")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDevolucion;
    private int idAdopcion;
    private int idMascota;
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    private String motivo;
    private String descripcionDetallada;
    private String estadoMascota;

    public Devolucion() {}

    public int getIdDevolucion() { return idDevolucion; }
    public void setIdDevolucion(int idDevolucion) { this.idDevolucion = idDevolucion; }
    public int getIdAdopcion() { return idAdopcion; }
    public void setIdAdopcion(int idAdopcion) { this.idAdopcion = idAdopcion; }
    public int getIdMascota() { return idMascota; }
    public void setIdMascota(int idMascota) { this.idMascota = idMascota; }
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDescripcionDetallada() { return descripcionDetallada; }
    public void setDescripcionDetallada(String d) { this.descripcionDetallada = d; }
    public String getEstadoMascota() { return estadoMascota; }
    public void setEstadoMascota(String estadoMascota) { this.estadoMascota = estadoMascota; }
}