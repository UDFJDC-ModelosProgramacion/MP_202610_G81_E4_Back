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
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VaccineService.class)
class VaccineServiceTest {

    @Autowired
    private VaccineService service;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VaccineEntity> vaccineList = new ArrayList<>();
    private List<VaccinationRecordEntity> recordList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VaccineEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VaccinationRecordEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VaccinationRecordEntity vaccinationRecord = new VaccinationRecordEntity();
            entityManager.persist(vaccinationRecord);
            recordList.add(vaccinationRecord);

            VaccineEntity vaccine = factory.manufacturePojo(VaccineEntity.class);
            vaccine.setVaccinationRecord(vaccinationRecord);
            vaccine.setVaccineName("Rabies");

            entityManager.persist(vaccine);
            vaccineList.add(vaccine);
        }
    }

    @Test
    void testCreateVaccine() throws Exception {
        VaccineEntity entity = factory.manufacturePojo(VaccineEntity.class);
        entity.setVaccinationRecord(recordList.get(0));
        entity.setVaccineName("Parvo");

        VaccineEntity result = service.createVaccine(entity);
        assertNotNull(result);
    }

    @Test
    void testCreateVaccineInvalid() {
        VaccineEntity entity = new VaccineEntity();
        assertThrows(IllegalOperationException.class, () -> service.createVaccine(entity));
    }

    @Test
    void testGetVaccines() {
        List<VaccineEntity> list = service.getVaccines();
        assertEquals(vaccineList.size(), list.size());
    }
}