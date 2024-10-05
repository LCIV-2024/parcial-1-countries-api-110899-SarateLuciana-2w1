package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.CountryRequest;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;

    @GetMapping("/countries")
    public List<CountryDTO> getAllCountries(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code) {

        List<Country> countries = countryService.getAllCountries();

        if (name != null && !name.isEmpty()) {
            countries = countries.stream()
                    .filter(country -> country.getName().equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        }

        if (code != null && !code.isEmpty()) {
            countries = countries.stream()
                    .filter(country -> country.getCode().equalsIgnoreCase(code))
                    .collect(Collectors.toList());
        }

        return countries.stream()
                .map(country -> new CountryDTO(country.getCode(), country.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/countries/{language}/language")
    public List<CountryDTO> getCountriesByLanguage(@PathVariable String language) {
        List<Country> countries = countryService.getAllCountries();

        countries = countries.stream()
                .filter(country -> country.getLanguages() != null && country.getLanguages().containsValue(language))
                .collect(Collectors.toList());

        return countries.stream()
                .map(country -> new CountryDTO(country.getCode(), country.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping("/countries/most-borders")
    public CountryDTO getCountryWithMostBorders() {
        List<Country> countries = countryService.getAllCountries();

        return countries.stream()
                .filter(country -> country.getBorders() != null)
                .max(Comparator.comparingInt(country -> country.getBorders().size()))
                .map(country -> new CountryDTO(country.getCode(), country.getName()))
                .orElse(null);
    }

    @PostMapping("/countries")
    public ResponseEntity<List<CountryDTO>> saveCountries(@RequestBody CountryRequest request) {
        if (request.getAmountOfCountryToSave() < 1 || request.getAmountOfCountryToSave() > 10) {
            return ResponseEntity.badRequest().build();
        }
        List<CountryDTO> savedCountries = countryService.saveRandomCountries(request.getAmountOfCountryToSave());
        return ResponseEntity.ok(savedCountries);
    }





}