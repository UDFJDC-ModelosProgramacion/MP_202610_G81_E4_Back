package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class VaccineDTO {

    private Long id;

    private String vaccineName;
    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private String batchNumber;
    private String observations;
    private Long vaccinationRecordId;

}