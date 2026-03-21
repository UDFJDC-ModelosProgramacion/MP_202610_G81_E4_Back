package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VaccineRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class VaccineService {

    @Autowired
    private VaccineRepository repository;

    @Autowired
    private VaccinationRecordRepository recordRepository;

    @Transactional
    public VaccineEntity createVaccine(VaccineEntity vaccine)
            throws IllegalOperationException {

        if (vaccine.getVaccineName() == null || vaccine.getVaccineName().isEmpty())
            throw new IllegalOperationException("Name is invalid");

        if (vaccine.getVaccinationRecord() == null)
            throw new IllegalOperationException("Record required");

        Optional<VaccinationRecordEntity> record =
                recordRepository.findById(vaccine.getVaccinationRecord().getId());

        if (record.isEmpty())
            throw new IllegalOperationException("Record not found");

        vaccine.setVaccinationRecord(record.get());
        return repository.save(vaccine);
    }

    @Transactional
    public List<VaccineEntity> getVaccines() {
        return repository.findAll();
    }
}