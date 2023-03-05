package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Driver;

import java.time.LocalDate;
import java.util.Optional;

public interface DriverService {
    Optional<Driver> findByDocument(String document);

    Long getDriverAgeOnBaseDate(final Driver driver, LocalDate baseDate);
}
