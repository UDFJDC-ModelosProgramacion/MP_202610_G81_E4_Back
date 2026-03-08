package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;


@Entity
@Table(name = "REVIEW_ENTITY")
@Data
public class ReviewEntity extends BaseEntity {
    private int review;
    private int pet;
    private int adopter;
    private int rating;
    private String comment;
    private Date reviewDate;
    private String[] photos;

    
    
}
