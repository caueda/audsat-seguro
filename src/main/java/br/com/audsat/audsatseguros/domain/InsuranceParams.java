package br.com.audsat.audsatseguros.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="insurance_params")
@Builder
@Entity
public class InsuranceParams implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="initial_age", nullable = false)
    private Integer initialAge;

    @Column(name="end_age", nullable = false)
    private Integer endAge;

    @Column(name="quote", nullable = false)
    private Double quote;

    @Column(name="aggravating_quote", nullable = false)
    private Double aggravatingQuote;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private Status status;
}
