package co.edu.udistrital.mdp.pets.services;

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
import co.edu.udistrital.mdp.pets.entities.VaccinationRecordEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VaccineService.class)
class VaccineServiceTest {

    @Autowired
    private VaccineService vaccineService;

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

        VaccineEntity result = vaccineService.createVaccine(entity);
        assertNotNull(result);
    }
    @Test
    void testCreateVaccinewWithValidRecordFlow() throws Exception {
        VaccinationRecordEntity vaccinationRecord = new VaccinationRecordEntity();
        entityManager.persist(vaccinationRecord);
        entityManager.flush();

        VaccineEntity entity = new VaccineEntity();
        entity.setVaccineName("Parvo");
        entity.setVaccinationRecord(vaccinationRecord);

        VaccineEntity result = vaccineService.createVaccine(entity);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(vaccinationRecord.getId(), result.getVaccinationRecord().getId());
    }

    @Test
    void testCreateVaccineInvalid() {
        VaccineEntity entity = new VaccineEntity();
        assertThrows(IllegalOperationException.class, () -> vaccineService.createVaccine(entity));
    }
    @Test
    void testCreatewithNullVaccinationRecord(){
        VaccineEntity entity = new VaccineEntity();
        entity.setVaccineName("rabia");
        entity.setVaccinationRecord(null);
        assertThrows(IllegalOperationException.class, () ->{
             vaccineService.createVaccine(entity);
        });

    }
    @Test
    void testGetVaccines() {
        List<VaccineEntity> list = vaccineService.getVaccines();
        assertEquals(vaccineList.size(), list.size());
    }

    @Test
    void testGetVaccinesByid() throws Exception {
        VaccineEntity entity = vaccineList.get(0);
        VaccineEntity result = vaccineService.getVaccine(entity.getId());

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void testGetVaccinesByidInvalid(){
        assertThrows(EntityNotFoundException.class, () ->{
            vaccineService.getVaccine(999L);
        });
    }

    @Test
    void testUpdateVaccine() throws EntityNotFoundException, IllegalOperationException{
        VaccineEntity entity = vaccineList.get(0);
        VaccineEntity updateData = factory.manufacturePojo(VaccineEntity.class);
        updateData.setApplicationDate(null);
        updateData.setBatchNumber(null);
        updateData.setVaccineName("Nombre actualizado de vacuna");
        updateData.setNextApplicationDate(null);
        updateData.setObservations("nueva observación");

        VaccineEntity result = vaccineService.updateVaccine(entity.getId(), updateData);

        assertNotNull(result);
        assertEquals("Nombre actualizado de vacuna", result.getVaccineName());
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void testUpdateVaccineInvalid(){
        assertThrows(EntityNotFoundException.class, () ->{
            VaccineEntity vaccineEntity = vaccineList.get(0);
            vaccineService.updateVaccine(999L, vaccineEntity);
        });
    }

    @Test
    void testDeleteVaccine() throws EntityNotFoundException{
        VaccineEntity entity = vaccineList.get(0);
        vaccineService.deleteVaccine(entity.getId());

        entityManager.flush();

        VaccineEntity deleted = entityManager.find(VaccineEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteVaccineInvalid(){
        assertThrows(EntityNotFoundException.class, () ->{
            vaccineService.deleteVaccine(999L);
        });
    }
}