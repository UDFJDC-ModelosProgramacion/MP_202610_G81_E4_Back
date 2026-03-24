package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.Adopter;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

@ExtendWith(MockitoExtension.class)
class AdopterServiceTest {

    @Mock
    private AdopterRepository adopterRepository;

    @InjectMocks
    private AdopterService adopterService;

    @Test
    void shouldCreateAdopterSuccessfully() {

        Adopter adopter = new Adopter();
        adopter.setEmail("test@mail.com");
        adopter.setAddress("Calle 123");
        adopter.setHousingType("Casa");

        when(adopterRepository.existsByEmail("test@mail.com"))
                .thenReturn(false);

        when(adopterRepository.save(any(Adopter.class)))
                .thenReturn(adopter);

        Adopter result = adopterService.createAdopter(adopter);

        assertNotNull(result);
        verify(adopterRepository).save(adopter);
    }

    @Test
    void shouldFailWhenHousingTypeInvalid() {

        Adopter adopter = new Adopter();
        adopter.setEmail("test@mail.com");
        adopter.setAddress("Calle 123");
        adopter.setHousingType("Hotel");

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> adopterService.createAdopter(adopter)
                );

        assertEquals("Invalid housing type", ex.getMessage());
    }
}