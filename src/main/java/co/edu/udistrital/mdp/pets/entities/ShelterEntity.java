package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelters")
@Data
@EqualsAndHashCode(callSuper = true)
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
    private List<String> photos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "shelter_videos", joinColumns = @JoinColumn(name = "shelter_id"))
    @Column(name = "video_url")
    private List<String> videos = new ArrayList<>();

    // Relación con Mascotas
    @PodamExclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetEntity> pets = new ArrayList<>();

    // Relación con Veterinarios
    @PodamExclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL)
    private List<VeterinarianEntity> veterinarians = new ArrayList<>();

    // Relación con Eventos
    @PodamExclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShelterEventEntity> events = new ArrayList<>();
}

    

