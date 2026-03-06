package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(length = 20)
    private String telefono;
    
    public void iniciarSesion() {
        System.out.println("Usuario " + nombre + " ha iniciado sesión");
    }
    
    public void cerrarSesion() {
        System.out.println("Usuario " + nombre + " ha cerrado sesión");
    }
    
    public void actualizarPerfil() {
        System.out.println("Perfil de " + nombre + " actualizado");
    }
}