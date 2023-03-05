package br.com.audsat.audsatseguros.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotEmpty(message = "car.model deve ser informado")
    @Column(name="model", nullable = false)
    private String model;

    @NotEmpty(message = "car.manufacturer deve ser informado")
    @Column(name="manufacturer", nullable = false)
    private String manufacturer;

    @NotEmpty(message = "car.year deve ser informado")
    @Column(name="car_year", nullable = false)
    private String year;

    @Column(name="fipe_value", nullable = false)
    private Double fipeValue;
}
