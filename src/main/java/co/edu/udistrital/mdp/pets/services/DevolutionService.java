package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.DevolutionEntity;
import co.edu.udistrital.mdp.pets.repositories.DevolucionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DevolutionService {

    @Autowired
    private DevolucionRepository repository;

    // Crear
    public DevolutionEntity createDevolution(DevolutionEntity devolution) {
        if (devolution.getAdoption() == null)
            throw new IllegalArgumentException("La devolución debe tener una adopción asociada");
        if (devolution.getReason() == null || devolution.getReason().isEmpty())
            throw new IllegalArgumentException("El motivo no puede ser nulo o vacío");
        List<DevolutionEntity> existing = repository.findByAdoption(devolution.getAdoption());
        if (!existing.isEmpty())
            throw new IllegalArgumentException("Ya existe una devolución para esta adopción");
        return repository.save(devolution);
    }

    // Obtener todos
    public List<DevolutionEntity> getDevolutions() {
        return repository.findAll();
    }

    // Obtener uno por ID
    public DevolutionEntity getDevolution(Long id) {
        Optional<DevolutionEntity> devolution = repository.findById(id.intValue());
        if (devolution.isEmpty())
            throw new IllegalArgumentException("La devolución con id " + id + " no existe");
        return devolution.get();
    }

    // Actualizar
    public DevolutionEntity updateDevolution(Long id, DevolutionEntity devolution) {
        Optional<DevolutionEntity> existing = repository.findById(id.intValue());
        if (existing.isEmpty())
            throw new IllegalArgumentException("La devolución con id " + id + " no existe");
        if (devolution.getReason() == null || devolution.getReason().isEmpty())
            throw new IllegalArgumentException("El motivo no puede ser nulo o vacío");
        DevolutionEntity toUpdate = existing.get();
        toUpdate.setReason(devolution.getReason());
        toUpdate.setDetailedDescription(devolution.getDetailedDescription());
        toUpdate.setPetState(devolution.getPetState());
        toUpdate.setReturnDate(devolution.getReturnDate());
        return repository.save(toUpdate);
    }

    // Eliminar
    public void deleteDevolution(Long id) {
        Optional<DevolutionEntity> devolution = repository.findById(id.intValue());
        if (devolution.isEmpty())
            throw new IllegalArgumentException("La devolución con id " + id + " no existe");
        repository.deleteById(id.intValue());
    }
}