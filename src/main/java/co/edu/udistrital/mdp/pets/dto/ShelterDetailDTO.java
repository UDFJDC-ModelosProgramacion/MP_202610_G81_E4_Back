package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShelterDetailDTO extends ShelterDTO {
    private String description;
    private List<String> photos = new ArrayList<>();
    private List<String> videos = new ArrayList<>();
    private List<Long> petIds = new ArrayList<>();
    private List<Long> veterinarianIds = new ArrayList<>();
    private List<Long> eventIds = new ArrayList<>();

    public ShelterDetailDTO(ShelterEntity entity) {
        super(entity); 
        if (entity != null) {
            this.description = entity.getDescription();
            if (entity.getPhotos() != null) {
                this.photos = new ArrayList<>(entity.getPhotos());
            }
            if (entity.getVideos() != null) {
                this.videos = new ArrayList<>(entity.getVideos());
            }
            if (entity.getPets() != null) {
                entity.getPets().forEach(p -> this.petIds.add(p.getId()));
            }
            if (entity.getVeterinarians() != null) {
                entity.getVeterinarians().forEach(v -> this.veterinarianIds.add(v.getId()));
            }
            if (entity.getEvents() != null) {
                entity.getEvents().forEach(e -> this.eventIds.add(e.getId()));
            }
        }
    }
}
