package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "shelters")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"pets", "veterinarians", "events"})
@NoArgsConstructor
@AllArgsConstructor
public class ShelterEntity extends BaseEntity {

    private String name;
    private String city;
    private String address;
    private String phone;
    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "shelter_photos", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @ElementCollection
    @CollectionTable(name = "shelter_videos", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "video_url")
    private List<String> videos;

    @PodamExclude
    @ToString.Exclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("shelter")
    private List<PetEntity> pets;

    @PodamExclude
    @ToString.Exclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("shelter")
    private List<VeterinarianEntity> veterinarians;

    @PodamExclude
    @ToString.Exclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("shelter")
    private List<ShelterEventEntity> events;
}

    

