package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.services.TrialStayService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(TrialStayService.class)
public class TrialStayServiceTest {

    @Autowired
    private TrialStayService trialStayService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<TrialStayEntity> trialStayList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TrialStayEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.flush();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {

            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);

            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            adopter.setFirstName("Nombre");
            adopter.setLastName("Apellido");
            entityManager.persist(adopter);

            AdoptionEntity adoption = new AdoptionEntity();
            adoption.setPet(pet);
            adoption.setAdopter(adopter);
            entityManager.persist(adoption);

            TrialStayEntity trialStay = new TrialStayEntity();
            trialStay.setPet(pet);
            trialStay.setAdoption(adoption);
            trialStay.setStartDate(LocalDate.now());
            trialStay.setEndDate(LocalDate.now().plusDays(3));

            entityManager.persist(trialStay);
            trialStayList.add(trialStay);
        }

        entityManager.flush();
    }

    @Test
    void testCreateTrialStay() {

        ShelterEntity shelter = entityManager.persist(factory.manufacturePojo(ShelterEntity.class));

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        adopter.setFirstName("Juan");
        adopter.setLastName("Perez");
        entityManager.persist(adopter);

        AdoptionEntity adoption = new AdoptionEntity();
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        entityManager.persist(adoption);

        TrialStayEntity entity = new TrialStayEntity();
        entity.setPet(pet);
        entity.setAdoption(adoption);
        entity.setStartDate(LocalDate.now());
        entity.setEndDate(LocalDate.now().plusDays(5));
        entity.setResult("OK");

        TrialStayEntity result = trialStayService.createTrialStay(entity);

        assertNotNull(result);
        assertNotNull(result.getId());

        TrialStayEntity found = entityManager.find(TrialStayEntity.class, result.getId());

        assertEquals(entity.getStartDate(), found.getStartDate());
        assertEquals(entity.getEndDate(), found.getEndDate());
        assertEquals("OK", found.getResult());
    }

    @Test
    void testCreateTrialStayInvalidDates() {

        TrialStayEntity entity = new TrialStayEntity();
        entity.setStartDate(null);
        entity.setEndDate(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> {
            trialStayService.createTrialStay(entity);
        });
    }

    @Test
    void testSearchTrialStay() {
        TrialStayEntity entity = trialStayList.get(0);

        TrialStayEntity result = trialStayService.searchTrialStay(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testSearchTrialStayNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            trialStayService.searchTrialStay(999L);
        });
    }
}

