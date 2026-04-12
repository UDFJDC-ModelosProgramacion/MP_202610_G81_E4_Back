package co.edu.udistrital.mdp.pets;

import java.time.LocalDate;
import java.util.ArrayList;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import uk.co.jemos.podam.api.PodamFactory;

public final class TestEntityFactory {

    private TestEntityFactory() {
    }

    public static ShelterEntity createShelter(PodamFactory factory) {
        return factory.manufacturePojo(ShelterEntity.class);
    }

    public static PetEntity createPet(PodamFactory factory, ShelterEntity shelter, String status) {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        pet.setStatus(status);
        // Keep relation empty to avoid persisting transient vaccination graphs.
        pet.setVaccinationRecords(new ArrayList<>());
        return pet;
    }

    public static AdopterEntity createAdopter(PodamFactory factory) {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        if (adopter.getFirstName() == null || adopter.getFirstName().isBlank()) {
            adopter.setFirstName("Nombre");
        }
        if (adopter.getLastName() == null || adopter.getLastName().isBlank()) {
            adopter.setLastName("Apellido");
        }
        return adopter;
    }

    public static AdoptionEntity createAdoption(PetEntity pet, AdopterEntity adopter, String status) {
        AdoptionEntity adoption = new AdoptionEntity();
        adoption.setPet(pet);
        adoption.setAdopter(adopter);
        adoption.setAdoptionDate(LocalDate.now());
        adoption.setStatus(status);
        return adoption;
    }
}