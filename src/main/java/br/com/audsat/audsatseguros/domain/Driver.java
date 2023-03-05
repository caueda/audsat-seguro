package br.com.audsat.audsatseguros.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Driver implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotEmpty(message="driver.document dever ser informado")
    @Column(name="document", nullable = false, unique = true)
    private String document;

    @Temporal(TemporalType.DATE)
    @Column(name="birthdate")
    private LocalDate birthDate;
}
