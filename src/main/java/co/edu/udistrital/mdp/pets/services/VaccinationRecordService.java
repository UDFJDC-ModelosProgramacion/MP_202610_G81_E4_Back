package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccinationRecordRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class VaccinationRecordService {

    @Autowired
    private VaccinationRecordRepository repository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public VaccinationRecordEntity createRecord(VaccinationRecordEntity record)
            throws IllegalOperationException {

        if (record.getPet() == null)
            throw new IllegalOperationException("Pet is required");

        Optional<PetEntity> pet = petRepository.findById(record.getPet().getId());
        if (pet.isEmpty())
            throw new IllegalOperationException("Pet not found");

        record.setPet(pet.get());
        return repository.save(record);
    }

    @Transactional
    public List<VaccinationRecordEntity> getRecords() {
        return repository.findAll();
    }
}