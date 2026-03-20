package co.test.java.co.edu.udistrital.test.java.co.edu.udistrital.mdp.pets.test.java.co.edu.udistrital.mdp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.TrialStayEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.services.TrialStayService;
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
        entityManager.getEntityManager().createQuery("delete from  TrialStayEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from  PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from  AdoptionEntity").executeUpdate();  
        entityManager.getEntityManager().createQuery("delete from  ShelterEntity").executeUpdate();
    }   
   private void insertData() {
    for (int i = 0; i < 3; i++) {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setPet(pet);
        entityManager.persist(adoption);

        TrialStayEntity trialStay = factory.manufacturePojo(TrialStayEntity.class);
        trialStay.setPet(pet);
        trialStay.setAdoption(adoption);
        trialStay.setStartDate(java.time.LocalDate.now());
        trialStay.setEndDate(java.time.LocalDate.now().plusDays(3));
        entityManager.persist(trialStay);
        trialStayList.add(trialStay);
    }
}
@Test
void testCreateTrialStay() {
    TrialStayEntity newEntity = factory.manufacturePojo(TrialStayEntity.class);
    PetEntity pet = factory.manufacturePojo(PetEntity.class);
    entityManager.persist(pet);

    AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
    entityManager.persist(adoption);
    newEntity.setPet(pet);
    newEntity.setAdoption(adoption);
    newEntity.setStartDate(java.time.LocalDate.now());
    newEntity.setEndDate(java.time.LocalDate.now().plusDays(5));

    TrialStayEntity result = trialStayService.createTrialStay(newEntity);
    assertNotNull(result);
    TrialStayEntity entity = entityManager.find(TrialStayEntity.class, result.getId());
    assertEquals(newEntity.getStartDate(), entity.getStartDate());
    assertEquals(newEntity.getEndDate(), entity.getEndDate());
    assertEquals(newEntity.getResult(), entity.getResult());
    assertEquals(newEntity.getObservations(), entity.getObservations());
}
@Test
void testCreateTrialStayInvalidDates() {
    assertThrows(IllegalArgumentException.class, () -> {
        TrialStayEntity entity = factory.manufacturePojo(TrialStayEntity.class);
        entity.setStartDate(null);
        trialStayService.createTrialStay(entity);
    });
}
@Test
void testSearchTrialStay() {
    TrialStayEntity entity = trialStayList.get(0);
    TrialStayEntity result = trialStayService.searchTrialStay(entity.getId());
    assertNotNull(result);
    assertEquals(entity.getId(), result.getId());
    assertEquals(entity.getStartDate(), result.getStartDate());
}
@Test
void testSearchTrialStayNotFound() {
    assertThrows(EntityNotFoundException.class, () -> {
        trialStayService.searchTrialStay(0L);
    });
}


}

