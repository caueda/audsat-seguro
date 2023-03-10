package br.com.audsat.audsatseguros.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotBlank(message = "customer.name deve ser informado")
    @Column(name="name", nullable = false)
    private String name;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name="driver_id")
    private Driver driver;
}
