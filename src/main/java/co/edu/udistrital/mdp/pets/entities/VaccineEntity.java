package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDate;

@Entity
@Table(name = "vaccines")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VaccineEntity extends BaseEntity {
    
    @Column(nullable = false)
    private String vaccineName;

    private LocalDate applicationDate;
    private LocalDate nextApplicationDate;
    private String batchNumber;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @PodamExclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccination_record_id")
    private VaccinationRecordEntity vaccinationRecord;
}