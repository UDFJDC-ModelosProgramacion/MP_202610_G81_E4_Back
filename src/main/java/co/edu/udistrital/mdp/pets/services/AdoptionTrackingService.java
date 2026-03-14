package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionTrackingEntity;
import co.edu.udistrital.mdp.pets.repositories.SeguimientoAdopcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdoptionTrackingService {

    @Autowired
    private SeguimientoAdopcionRepository repository;

    // Crear
    public AdoptionTrackingEntity createAdoptionTracking(AdoptionTrackingEntity tracking) {
        if (tracking.getAdoption() == null)
            throw new IllegalArgumentException("El seguimiento debe tener una adopción asociada");
        if (tracking.getFrequency() == null || tracking.getFrequency().isEmpty())
            throw new IllegalArgumentException("La frecuencia no puede ser nula o vacía");
        return repository.save(tracking);
    }

    // Obtener todos
    public List<AdoptionTrackingEntity> getAdoptionTrackings() {
        return repository.findAll();
    }

    // Obtener uno por ID
    public AdoptionTrackingEntity getAdoptionTracking(Long id) {
        Optional<AdoptionTrackingEntity> tracking = repository.findById(id.intValue());
        if (tracking.isEmpty())
            throw new IllegalArgumentException("El seguimiento con id " + id + " no existe");
        return tracking.get();
    }

    // Actualizar
    public AdoptionTrackingEntity updateAdoptionTracking(Long id, AdoptionTrackingEntity tracking) {
        Optional<AdoptionTrackingEntity> existing = repository.findById(id.intValue());
        if (existing.isEmpty())
            throw new IllegalArgumentException("El seguimiento con id " + id + " no existe");
        if (tracking.getNextReview() == null)
            throw new IllegalArgumentException("La fecha de próxima revisión no puede ser nula");
        AdoptionTrackingEntity toUpdate = existing.get();
        toUpdate.setFrequency(tracking.getFrequency());
        toUpdate.setNotes(tracking.getNotes());
        toUpdate.setNextReview(tracking.getNextReview());
        return repository.save(toUpdate);
    }

    // Eliminar
    public void deleteAdoptionTracking(Long id) {
        Optional<AdoptionTrackingEntity> tracking = repository.findById(id.intValue());
        if (tracking.isEmpty())
            throw new IllegalArgumentException("El seguimiento con id " + id + " no existe");
        repository.deleteById(id.intValue());
    }
}