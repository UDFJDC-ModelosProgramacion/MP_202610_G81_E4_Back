package co.edu.udistrital.mdp.pets.services;

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

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(AdoptionRequestService.class)
class AdoptionRequestServiceTest {

    @Autowired
    private AdoptionRequestService service;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AdoptionRequestEntity> requestList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();
    private List<AdopterEntity> adopterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);
            adopterList.add(adopter);

            AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
            request.setPet(pet);
            request.setAdopter(adopter);
            request.setStatus("PENDING");

            entityManager.persist(request);
            requestList.add(request);
        }
    }

    @Test
    void testCreate() throws Exception {
        AdoptionRequestEntity entity = factory.manufacturePojo(AdoptionRequestEntity.class);
        entity.setPet(petList.get(0));
        entity.setAdopter(adopterList.get(0));
        entity.setStatus("PENDING");

        AdoptionRequestEntity result = service.createAdoptionRequest(entity);

        assertNotNull(result);
    }

    @Test
    void testCreateInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            AdoptionRequestEntity entity = new AdoptionRequestEntity();
            service.createAdoptionRequest(entity);
        });
    }
}