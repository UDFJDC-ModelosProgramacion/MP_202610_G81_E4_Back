package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionHistoryEntity;
import co.edu.udistrital.mdp.pets.repositories.HistorialAdopcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdoptionHistoryService {

    @Autowired
    private HistorialAdopcionRepository repository;

    // Crear
    public AdoptionHistoryEntity createAdoptionHistory(AdoptionHistoryEntity history) {
        if (history.getAdoption() == null)
            throw new IllegalArgumentException("El historial debe tener una adopción asociada");
        if (history.getReason() == null || history.getReason().isEmpty())
            throw new IllegalArgumentException("La razón no puede ser nula o vacía");
        return repository.save(history);
    }

    // Obtener todos
    public List<AdoptionHistoryEntity> getAdoptionHistories() {
        return repository.findAll();
    }

    // Obtener uno por ID
    public AdoptionHistoryEntity getAdoptionHistory(Long id) {
        Optional<AdoptionHistoryEntity> history = repository.findById(id.intValue());
        if (history.isEmpty())
            throw new IllegalArgumentException("El historial con id " + id + " no existe");
        return history.get();
    }

    // Actualizar
    public AdoptionHistoryEntity updateAdoptionHistory(Long id, AdoptionHistoryEntity history) {
        Optional<AdoptionHistoryEntity> existing = repository.findById(id.intValue());
        if (existing.isEmpty())
            throw new IllegalArgumentException("El historial con id " + id + " no existe");
        if (history.getReason() == null || history.getReason().isEmpty())
            throw new IllegalArgumentException("La razón no puede ser nula o vacía");
        AdoptionHistoryEntity toUpdate = existing.get();
        toUpdate.setReason(history.getReason());
        toUpdate.setDetail(history.getDetail());
        toUpdate.setDate(history.getDate());
        toUpdate.setAdoption(history.getAdoption());
        return repository.save(toUpdate);
    }

    // Eliminar
    public void deleteAdoptionHistory(Long id) {
        Optional<AdoptionHistoryEntity> history = repository.findById(id.intValue());
        if (history.isEmpty())
            throw new IllegalArgumentException("El historial con id " + id + " no existe");
        repository.deleteById(id.intValue());
    }
}