package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.CountryDTO;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.model.CountryEntity;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;
        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        /**
         * Mapeo de campo cca3 (String) y campos borders ((List<String>))
         */
        public Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                String code = (String) countryData.get("cca3");

                List<String> borders = (List<String>) countryData.get("borders");

                return Country.builder()
                        .code(code)
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .borders(borders)
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }

        public CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }


        public List<CountryDTO> saveRandomCountries(int amount) {
                List<Country> countries = getAllCountries();
                Collections.shuffle(countries);
                List<Country> selectedCountries = countries.subList(0, amount);
                List<CountryDTO> savedCountries = new ArrayList<>();
                for (Country country : selectedCountries) {
                        CountryEntity countryEntity = new CountryEntity();
                        countryEntity.setName(country.getName());
                        countryEntity.setCode(country.getCode());
                        countryEntity.setPopulation(country.getPopulation());
                        countryEntity.setArea(country.getArea());
                        countryRepository.save(countryEntity);
                        savedCountries.add(mapToDTO(country));
                }

                return savedCountries;
        }


}
