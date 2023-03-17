package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Driver;
import br.com.audsat.audsatseguros.repository.DriverRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    @Override
    public Optional<Driver> findByDocument(String document) {
        return driverRepository.findByDocument(document);
    }

    @Override
    public Long getDriverAgeOnBaseDate(final Driver driver, LocalDate baseDate) {
        return ChronoUnit.YEARS.between(driver.getBirthDate(), baseDate);
    }
}
