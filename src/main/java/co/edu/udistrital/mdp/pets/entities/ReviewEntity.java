
package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String comments;
    private Integer rating;
    private LocalDateTime reviewDate;

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;
}