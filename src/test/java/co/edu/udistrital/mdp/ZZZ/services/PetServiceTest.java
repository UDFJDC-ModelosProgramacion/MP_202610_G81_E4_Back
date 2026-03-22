package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.services.PetService;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PetService.class)
public class PetServiceTest {
    @Autowired
    private PetService petService;
    @Autowired
    private TestEntityManager entityManager;
    private PodamFactory factory = new PodamFactoryImpl();
    private List<PetEntity> petList = new ArrayList<>();
    private List<ShelterEntity> shelterList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData(){
        for(int i = 0; i<3; i++){
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);
            shelterList.add(shelter);
        }

        for(int i = 0; i<3; i++){
            PetEntity pet = factory.manufacturePojo((PetEntity.class));
            pet.setShelter(shelterList.get(0));
            entityManager.persist(pet);
            petList.add(pet);
        }
    }
    @Test
    void testCreatePet() throws IllegalOperationException {
        PetEntity newPet = factory.manufacturePojo((PetEntity.class));
        newPet.setShelter(shelterList.get(0));
        newPet.setName("Rex");
        newPet.setSpecies("Dog");
        newPet.setStatus("AVAILABLE");

        PetEntity result = petService.createPet(newPet);
        assertNotNull(result);

        PetEntity entity = entityManager.find(PetEntity.class, result.getId());
        assertEquals(newPet.getName(), entity.getName());
        assertEquals(newPet.getSpecies(), entity.getSpecies());
    }
    @Test
    void testCreatePetWithInvalidName(){
        assertThrows(IllegalOperationException.class,() -> {
            PetEntity newPet = factory.manufacturePojo(PetEntity.class);
            newPet.setShelter(shelterList.get(0));
            newPet.setName("");
            newPet.setSpecies("Dog");
            newPet.setStatus("AVALIABLE");

            petService.createPet(newPet);
        });
    }
    @Test
    void testCreatePetWithInvalidShelter(){
        assertThrows(IllegalOperationException.class,() -> {
            PetEntity newPet = factory.manufacturePojo(PetEntity.class);
            newPet.setName("");
            newPet.setSpecies("Dog");
            newPet.setStatus("AVALIABLE");
            ShelterEntity fake = new ShelterEntity();
            fake.setId(0L);
            newPet.setShelter(fake);
            petService.createPet(newPet);
        });
    }
    @Test
    void testGetPets(){
        List<PetEntity> list = petService.getPets();
        assertEquals(petList.size(), list.size());
    }
    @Test
    void testGetPet() throws EntityNotFoundException{
        PetEntity entity = petList.get(0);
        PetEntity result = petService.getPet(entity.getId());
        
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }
    @Test
    void testGetInvalidPet(){
        assertThrows(EntityNotFoundException.class, () -> {
            petService.getPet(0L);
        });
    }
    @Test
    void testUpdatePet()
        throws EntityNotFoundException, IllegalOperationException{
            PetEntity entity = petList.get(0);

            PetEntity updated = factory.manufacturePojo(PetEntity.class);
            updated.setName("Nuevo nombre");
            PetEntity result = petService.updatePet(entity.getId(), updated);
            assertEquals(("Nuevo nombre"), result.getName());
    }
    @Test
    void testUpdateInvalidPet(){
        assertThrows(EntityNotFoundException.class, () -> {
            PetEntity pet = factory.manufacturePojo(PetEntity.class );
            petService.updatePet(0L, pet);
        });
    }
    @Test
    void testDeletePet() throws Exception{
        PetEntity entity = petList.get(1);
        petService.deletePet(entity.getId());
        PetEntity deleted = entityManager.find(PetEntity.class, entity.getId());
        assertNull(deleted);
    }
    @Test
    void testDeletePetWithVaccinationRecords(){
        assertThrows(IllegalOperationException.class, ()-> {
            PetEntity pet = petList.get(0);
            VaccinationRecordEntity record = new VaccinationRecordEntity();
            record.setPet(pet);

            entityManager.persist(record);
            pet.getVaccinationRecords().add(record);

            petService.deletePet(pet.getId());
        });
    }
    
}
