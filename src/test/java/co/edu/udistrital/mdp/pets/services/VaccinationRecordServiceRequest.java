package co.edu.udistrital.mdp.pets.services;

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

import co.edu.udistrital.mdp.pets.services.VaccinationRecordService;
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VaccinationRecordService.class)
class VaccinationRecordServiceTest {

    @Autowired
    private VaccinationRecordService service;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VaccinationRecordEntity> recordList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VaccinationRecordEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            VaccinationRecordEntity vaccinationRecord = new VaccinationRecordEntity();
            vaccinationRecord.setPet(pet);

            entityManager.persist(vaccinationRecord);
            recordList.add(vaccinationRecord);
        }
    }

    @Test
    void testCreateRecord() throws Exception {
        VaccinationRecordEntity entity = new VaccinationRecordEntity();
        entity.setPet(petList.get(0));

        VaccinationRecordEntity result = service.createRecord(entity);

        assertNotNull(result);
    }

    @Test
    void testCreateRecordInvalid() {
        assertThrows(IllegalOperationException.class, () -> {
            VaccinationRecordEntity entity = new VaccinationRecordEntity();
            service.createRecord(entity);
        });
    }

    @Test
    void testGetRecords() {
        List<VaccinationRecordEntity> list = service.getRecords();
        assertEquals(recordList.size(), list.size());
    }
}