package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdopterService {
    private static final String ADOPTER_NOT_FOUND = "Adopter not found with id: ";

    @Autowired
    private AdopterRepository adopterRepository;
    @Autowired
    @Lazy
    private AdopterService self;

    private static final List<String> VALID_HOUSING = Arrays.asList("Casa", "Apartamento", "Finca");

    @Transactional
    public AdopterEntity createAdopter(AdopterEntity adopter) {
        log.info("Creating adopter: {} {}", adopter.getFirstName(), adopter.getLastName());
        if (!VALID_HOUSING.contains(adopter.getHousingType())) {
            throw new IllegalArgumentException("Invalid housing type");
        }
        return adopterRepository.save(adopter);
    }

    @Transactional(readOnly = true)
    public List<AdopterEntity> searchAdopters() {
        log.info("Searching all adopters");
        return adopterRepository.findAll().stream().toList();
    }

    @Transactional(readOnly = true)
    public AdopterEntity searchAdopter(Long id) {
        return adopterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADOPTER_NOT_FOUND + id));
    }

    @Transactional
    public AdopterEntity updateAdopter(Long id, AdopterEntity adopter) {
        log.info("Updating adopter with id: {}", id);
        AdopterEntity existing = self.searchAdopter(id);
        
        existing.setFirstName(adopter.getFirstName());
        existing.setLastName(adopter.getLastName());
        existing.setHousingType(adopter.getHousingType());

        return adopterRepository.save(existing);
    }

    @Transactional
    public void deleteAdopter(Long id) {
        log.info("Deleting adopter with id: {}", id);
        AdopterEntity adopter = self.searchAdopter(id);

        if (adopter.getAdoptionRequests() != null && !adopter.getAdoptionRequests().isEmpty()) {
            throw new IllegalStateException("Cannot delete adopter with active adoption requests");
        }

        adopterRepository.delete(adopter);
    }
}
