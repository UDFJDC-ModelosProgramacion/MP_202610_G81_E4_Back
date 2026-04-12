package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public AdopterEntity createAdopter(AdopterEntity adopter) {
        if (adopter == null) {
            throw new IllegalArgumentException("El adoptante no puede ser nulo");
        }

        log.info("Creando adoptante: {} {}", adopter.getFirstName(), adopter.getLastName());

        validateRequiredFields(adopter);

        if (adopter.getHousingType() != null) {
            String ht = adopter.getHousingType().toUpperCase().trim();
            List<String> validHousing = Arrays.asList("CASA", "APARTAMENTO", "FINCA");

            if (!validHousing.contains(ht)) {
                throw new IllegalArgumentException("Tipo de vivienda inválido");
            }

            adopter.setHousingType(ht.substring(0, 1) + ht.substring(1).toLowerCase());
        }

        return adopterRepository.save(adopter);
    }

    @Transactional(readOnly = true)
    public AdopterEntity searchAdopter(Long id) {
        return adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No encontrado"));
    }

    @Transactional(readOnly = true)
    public List<AdopterEntity> searchAdopters() {
        return adopterRepository.findAll();
    }

    @Transactional
    public AdopterEntity updateAdopter(Long id, AdopterEntity adopter) {

        AdopterEntity existing = searchAdopter(id);

        existing.setFirstName(adopter.getFirstName());
        existing.setLastName(adopter.getLastName());
        existing.setAddress(adopter.getAddress());
        existing.setHousingType(adopter.getHousingType());
        existing.setHasChildren(adopter.getHasChildren());
        existing.setHasOtherPets(adopter.getHasOtherPets());

        if (adopter.getPreferences() != null) {
            existing.setPreferences(new ArrayList<>(adopter.getPreferences()));
        }

        return adopterRepository.save(existing);
    }

    @Transactional
    public void deleteAdopter(Long id) {

        AdopterEntity adopter = searchAdopter(id);

        if (adopter.getAdoptionRequests() != null && !adopter.getAdoptionRequests().isEmpty()) {
            throw new IllegalStateException("Tiene solicitudes asociadas");
        }

        adopterRepository.delete(adopter);
    }

    private void validateRequiredFields(AdopterEntity adopter) {
        if (adopter.getFirstName() == null || adopter.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre obligatorio");
        }
        if (adopter.getLastName() == null || adopter.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Apellido obligatorio");
        }
    }
}
