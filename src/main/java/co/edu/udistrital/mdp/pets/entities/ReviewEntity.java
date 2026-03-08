
package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity extends BaseEntity {

    private String comments;
    private Integer rating;
    private LocalDateTime reviewDate;

    @OneToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "adoption_id")
    private AdoptionEntity adoption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewEntity)) return false;
        ReviewEntity that = (ReviewEntity) o;
        return getId() != null && getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}