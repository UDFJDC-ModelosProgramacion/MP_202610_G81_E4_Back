package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AdoptionRequestDTO {

    private Long id;

    private LocalDate requestDate;
    private String status;
    private String motivation;
    private Long petId;
    private Long AdopterId;

}