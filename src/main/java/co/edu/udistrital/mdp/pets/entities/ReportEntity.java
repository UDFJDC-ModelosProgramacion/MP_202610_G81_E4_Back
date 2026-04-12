package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"shelter"})
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity extends BaseEntity {

    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    
    @Column(columnDefinition = "TEXT")
    private String data;
    
    private LocalDate generationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;
}