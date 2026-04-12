package co.edu.udistrital.mdp.pets.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdopterDetailDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String lastName;
    private String address;
    private String housingType;
    private Boolean hasChildren;
    private Boolean hasOtherPets;
    private List<String> preferences;
    
    // Asociaciones (cuando existan las entidades de otros miembros)
    // private List<AdoptionRequestDTO> adoptionRequests;
    // private List<AdoptionDTO> adoptions;
}