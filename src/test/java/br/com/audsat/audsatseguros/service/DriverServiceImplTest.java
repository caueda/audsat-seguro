package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Driver;
import br.com.audsat.audsatseguros.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DriverServiceImplTest {
    @Mock
    DriverRepository driverRepository;

    DriverService driverService;

    @BeforeEach
    void setUp() {
        this.driverService = new DriverServiceImpl(driverRepository);
    }

    @Test
    void testGetDriverAgeOnBaseDate() {
        Long expectedAgeResult = 41L;
        Driver driver = Driver.builder()
                .id(1L)
                .birthDate(LocalDate.of(1982,1,1))
                .document("123")
                .build();
        var driverAge = driverService.getDriverAgeOnBaseDate(driver, LocalDate.of(2023, 3, 5));
        assertEquals(expectedAgeResult, driverAge);
    }
}