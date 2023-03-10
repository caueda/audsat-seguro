package br.com.audsat.audsatseguros.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Insurance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_dt")
    private LocalDateTime creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_dt")
    private LocalDateTime updatedDate;

    @ManyToOne(cascade = {CascadeType.REFRESH})
    @JoinColumn(name="car_id")
    private Car car;

    @Column(name="quote")
    private Double quote;

    @Column(name="insurance_value")
    private Double insuranceValue;

    @Column(name="is_active")
    private boolean active;

    @Enumerated(EnumType.STRING)
    @Column(name="INSURANCE_STATUS")
    private InsuranceStatus insuranceStatus;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
