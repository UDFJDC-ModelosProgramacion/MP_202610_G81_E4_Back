package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void testCreateWithNullPet(){
        AdoptionRequestEntity entity = factory.manufacturePojo((AdoptionRequestEntity.class));
        entity.setPet(null);
        entity.setAdopter(adopterList.get(0));
        entity.setStatus("PENDING");
        assertThrows(IllegalOperationException.class, () ->{
            service.createAdoptionRequest(entity);
        });
    }
    @Test
    void testCreateWithEmptyStatus(){
        AdoptionRequestEntity entity = factory.manufacturePojo(AdoptionRequestEntity.class);
        entity.setPet(petList.get(0));
        entity.setAdopter(adopterList.get(0));
        entity.setStatus("");

        assertThrows(IllegalOperationException.class, () ->{
            service.createAdoptionRequest(entity);
        });
    }
    @Test
    void testCreateWithNonExistingAdopter(){
        AdoptionRequestEntity entity = factory.manufacturePojo(AdoptionRequestEntity.class);
        AdopterEntity fakeAdopter = factory.manufacturePojo(AdopterEntity.class);
        fakeAdopter.setId(999L);//no esta persistido
        entity.setPet(petList.get(0));
        entity.setAdopter(fakeAdopter);
        entity.setStatus("PENDING");

        assertThrows(IllegalOperationException.class, () ->{
            service.createAdoptionRequest(entity);
        });

    }
    @Test
    void testCreateWithNonExistingPet(){
        AdoptionRequestEntity entity = factory.manufacturePojo(AdoptionRequestEntity.class);
        PetEntity fakePet = factory.manufacturePojo(PetEntity.class);
        fakePet.setId(999L);//no esta persistido
        entity.setPet(fakePet);
        entity.setAdopter(adopterList.get(0));
        entity.setStatus("PENDING");
        assertThrows(IllegalOperationException.class,() ->{
            service.createAdoptionRequest(entity);
        });
    }

    @Test
    void testCreateInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            AdoptionRequestEntity entity = new AdoptionRequestEntity();
            service.createAdoptionRequest(entity);
        });
    }
    @Test
    void testGetRequest() throws Exception{
        AdoptionRequestEntity existing = requestList.get(0);

        AdoptionRequestEntity result = service.getRequest(existing.getId());

        assertNotNull(result);
    }
    @Test
    void testGetRequestNotFound() {
        assertThrows(Exception.class, () -> {
            service.getRequest(999L);
        });
    }
    @Test
    void testUpdateSuccess()throws Exception{
        AdoptionRequestEntity entity = requestList.get(0);
        AdoptionRequestEntity updateData = new AdoptionRequestEntity();
        updateData.setStatus("APPROVED");
        updateData.setMotivation("Updated motivation for testing");
        updateData.setRequestDate(LocalDate.now());

        AdoptionRequestEntity result = service.updateAdoptionRequest(entity.getId(), updateData);
        assertNotNull(result);
        AdoptionRequestEntity updated = entityManager.find(AdoptionRequestEntity.class, entity.getId());
        assertEquals("APPROVED", updated.getStatus());
        assertEquals("Updated motivation for testing", updated.getMotivation());
    }
    @Test
    void testUpdateWithNullStatus(){
        AdoptionRequestEntity updateData = new AdoptionRequestEntity();
        updateData.setStatus(null);
        
        assertThrows(IllegalOperationException.class, () ->{
            service.updateAdoptionRequest(requestList.get(0).getId(), updateData);
        });
    }
    @Test 
    void testDeleteRequest()throws Exception{
        AdoptionRequestEntity request = requestList.get(0);
        Long requestId = request.getId();
        service.deleteRequest(requestId);
        entityManager.flush();
        AdoptionRequestEntity deleted = entityManager.find(AdoptionRequestEntity.class, requestId);
        assertNull(deleted);
    }
    @Test
    void testDeleteRequestNotFound() {
        assertThrows(Exception.class, () ->{
            service.deleteRequest((999L));
        });
    }

    @Test
    void testDeleteApprovedRequest() {
        AdoptionRequestEntity request = requestList.get(0);
        request.setStatus("APPROVED");
        entityManager.persist(request);
        entityManager.flush();
        entityManager.clear();
        Long requestId = request.getId();

        assertThrows(IllegalOperationException.class, () ->{
            service.deleteRequest(requestId);
        });
            
    }
}