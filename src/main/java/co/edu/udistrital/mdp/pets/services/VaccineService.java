package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VaccineRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VaccineService {
    private static final String VACCINE_NOT_FOUND = "Vacuna no encontrada con ID: ";
    private static final String INVALID_VACCINE_NAME = "El nombre de la vacuna es inválido.";

    @Autowired
    private VaccineRepository repository;

    @Autowired
    private VaccinationRecordRepository recordRepository;

    @Transactional
    public VaccineEntity createVaccine(VaccineEntity vaccineEntity) throws IllegalOperationException {
        log.info("Iniciando creación de vacuna: {}", vaccineEntity.getVaccineName());

        if (vaccineEntity.getVaccineName() == null || vaccineEntity.getVaccineName().isEmpty()) {
            throw new IllegalOperationException(INVALID_VACCINE_NAME);
        }

        if (vaccineEntity.getVaccinationRecord() == null || vaccineEntity.getVaccinationRecord().getId() == null) {
            throw new IllegalOperationException("Se requiere un registro de vacunación (VaccinationRecord).");
        }

        VaccinationRecordEntity vRecord = recordRepository.findById(vaccineEntity.getVaccinationRecord().getId())
                .orElseThrow(() -> new IllegalOperationException("El registro de vacunación no existe."));

        vaccineEntity.setVaccinationRecord(vRecord);
        return repository.save(vaccineEntity);
    }

    @Transactional(readOnly = true)
    public List<VaccineEntity> getVaccines() {
        log.info("Consultando todas las vacunas");
        return repository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public VaccineEntity getVaccine(Long id) {
        log.info("Buscando vacuna con ID: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(VACCINE_NOT_FOUND + id));
    }

    @Transactional
    public VaccineEntity updateVaccine(Long id, VaccineEntity vaccineDetails) throws IllegalOperationException {
        log.info("Actualizando vacuna con ID: {}", id);
        VaccineEntity existing = getVaccine(id);

        existing.setVaccineName(vaccineDetails.getVaccineName());
        existing.setApplicationDate(vaccineDetails.getApplicationDate());
        existing.setNextApplicationDate(vaccineDetails.getNextApplicationDate());
        existing.setBatchNumber(vaccineDetails.getBatchNumber());
        existing.setObservations(vaccineDetails.getObservations());

        return repository.save(existing);
    }

    @Transactional
    public void deleteVaccine(Long id) {
        log.info("Eliminando vacuna con ID: {}", id);
        VaccineEntity vaccineToDelete = getVaccine(id);
        repository.delete(vaccineToDelete);
    }
}