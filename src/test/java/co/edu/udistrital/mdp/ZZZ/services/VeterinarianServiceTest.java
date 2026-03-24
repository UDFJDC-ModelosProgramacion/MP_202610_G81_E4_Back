package co.edu.udistrital.mdp.ZZZ.services;

import co.edu.udistrital.mdp.pets.entities.Veterinarian;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeterinarianServiceTest {

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @InjectMocks
    private VeterinarianService veterinarianService;


    @Test
    void shouldCreateVeterinarianSuccessfully() {

        Veterinarian vet = new Veterinarian();
        vet.setVeterinarianId("LIC123");
        vet.setShelterId(1L);
        vet.setAvailability("Mon-Fri");
        vet.setSpecialties(List.of("General"));

        when(veterinarianRepository.save(any(Veterinarian.class)))
                .thenReturn(vet);

        Veterinarian result =
                veterinarianService.createVetterinarian(vet);

        assertNotNull(result);
        verify(veterinarianRepository).save(vet);
    }

    @Test
    void shouldFailWhenSpecialtyInvalid() {

        Veterinarian vet = new Veterinarian();
        vet.setVeterinarianId("LIC123");
        vet.setShelterId(1L);
        vet.setAvailability("Mon-Fri");
        vet.setSpecialties(List.of("Neurology")); // inválida

        assertThrows(
                IllegalArgumentException.class,
                () -> veterinarianService.createVetterinarian(vet)
        );
    }
}