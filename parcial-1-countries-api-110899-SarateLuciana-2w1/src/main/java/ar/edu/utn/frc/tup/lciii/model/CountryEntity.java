package ar.edu.utn.frc.tup.lciii.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CountryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    private Long id;

    private String name;
    private String code; // Agregar código
    private Long population; // Agregar población
    private Double area; // Agregar área

    // Constructores
    public CountryEntity() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
