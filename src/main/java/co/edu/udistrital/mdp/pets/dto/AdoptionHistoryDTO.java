package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;
import lombok.Data;

@Data
public class AdoptionHistoryDTO {
    private Long id;
    private String reason;
    private String detail;
    private Date date;
}