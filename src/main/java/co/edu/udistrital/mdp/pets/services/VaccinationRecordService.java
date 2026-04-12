package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class VaccinationRecordService {
    private static final String PET_NOT_FOUND = "Pet not found with ID: %d";

    @Autowired
    private VaccinationRecordRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public VaccinationRecordEntity createRecord(VaccinationRecordEntity vaccinationRecord)
            throws IllegalOperationException {
        log.info("Iniciando creación de registro de vacunación");

        if (vaccinationRecord.getPet() == null || vaccinationRecord.getPet().getId() == null) {
            log.error("Error al intentar crear registro de vacunación: Pet ID es obligatorio");
            throw new IllegalOperationException("Pet is required with a valid ID");
        }

        Long petId = vaccinationRecord.getPet().getId();
        Optional<PetEntity> pet = petRepository.findById(petId);
        if (pet.isEmpty()) {
            log.error("Error al intentar crear registro de vacunación: Mascota con ID {} no encontrada", petId);
            throw new IllegalOperationException(String.format(PET_NOT_FOUND, petId));
        }

        vaccinationRecord.setPet(pet.get());
        log.info("Registro de vacunación creado exitosamente");
        return repository.save(vaccinationRecord);
    }

    @Transactional(readOnly = true)
    public List<VaccinationRecordEntity> getRecords() {
        log.info("Buscando todos los registros de vacunación");
        return repository.findAll().stream().toList();
    }
}