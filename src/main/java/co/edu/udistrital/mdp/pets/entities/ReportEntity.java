package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Entity
@Table(name = "REPORT_ENTITY")
@Data
public class ReportEntity extends BaseEntity {

    private String reportType;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private String data;
    @Temporal(TemporalType.DATE)
    private Date generationDate;

    @ManyToOne
    @JoinColumn(name = "Shelter_id")
    private ShelterEntity shelter;
}