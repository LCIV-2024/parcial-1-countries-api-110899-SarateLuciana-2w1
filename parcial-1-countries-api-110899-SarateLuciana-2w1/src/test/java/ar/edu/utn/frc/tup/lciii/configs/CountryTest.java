package ar.edu.utn.frc.tup.lciii.configs;
import ar.edu.utn.frc.tup.lciii.controllers.CountryController;
import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.CountryRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryEntity;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@SpringBootTest
public class CountryTest {

    @InjectMocks
    private CountryController countryController;

    @Mock
    private CountryService countryService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCountries_noFilter_returnsAllCountries() {
        Country country = new Country("Argentina", 45195777, 2780400.0, "ARG", "Americas", List.of("BRA", "CHL"), Map.of("es", "Español"));
        when(countryService.getAllCountries()).thenReturn(Collections.singletonList(country));
        List<CountryDTO> result = countryController.getAllCountries(null, null);
        assertEquals(1, result.size());
        assertEquals("ARG", result.get(0).getCode());
        assertEquals("Argentina", result.get(0).getName());
    }

    @Test
    void getCountriesByLanguage_validLanguage_returnsFilteredCountries() {
        Country country = new Country("Argentina", 45195777, 2780400.0, "ARG", "Americas", null, Map.of("es", "Español"));
        when(countryService.getAllCountries()).thenReturn(Collections.singletonList(country));
        List<CountryDTO> result = countryController.getCountriesByLanguage("es");
        assertEquals(1, result.size());
        assertEquals("ARG", result.get(0).getCode());
        assertEquals("Argentina", result.get(0).getName());
    }

    @Test
    void getCountryWithMostBorders_returnsCountryWithMostBorders() {
        Country countryWithBorders = new Country("Argentina", 45195777, 2780400.0, "ARG", "Americas", List.of("BRA"), Map.of("es", "Español"));
        Country countryWithoutBorders = new Country("Brasil", 213993437, 8515767.0, "BRA", "Americas", null, Map.of("pt", "Portugués"));
        when(countryService.getAllCountries()).thenReturn(List.of(countryWithBorders, countryWithoutBorders));
        CountryDTO result = countryController.getCountryWithMostBorders();
        assertEquals("ARG", result.getCode());
        assertEquals("Argentina", result.getName());
    }

    @Test
    void saveCountries_invalidAmount_returnsBadRequest() {
        CountryRequest request = new CountryRequest();
        request.setAmountOfCountryToSave(0);
        ResponseEntity<List<CountryDTO>> result = countryController.saveCountries(request);
        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    void saveCountries_validAmount_returnsSavedCountries() {
        CountryRequest request = new CountryRequest();
        request.setAmountOfCountryToSave(5);
        when(countryService.saveRandomCountries(5)).thenReturn(Collections.emptyList());
        ResponseEntity<List<CountryDTO>> result = countryController.saveCountries(request);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(Collections.emptyList(), result.getBody());
    }

    @Test
    void getAllCountries_restTemplateCall_returnsCountries() {
        when(restTemplate.getForObject(anyString(), eq(List.class))).thenReturn(Collections.emptyList());
        List<Country> result = countryService.getAllCountries();
        assertEquals(0, result.size());
    }
}