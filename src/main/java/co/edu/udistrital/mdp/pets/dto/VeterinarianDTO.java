package co.edu.udistrital.mdp.pets.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeterinarianDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String lastName;
    private String availability;
    private Long shelterId; // Solo el ID, no el objeto Shelter
}