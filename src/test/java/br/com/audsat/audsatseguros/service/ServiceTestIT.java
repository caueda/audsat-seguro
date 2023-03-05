package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Driver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class ServiceTestIT {

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarDriverService carDriverService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByDocument() {
        Optional<Driver> optionalDriver = driverService.findByDocument("123.456.789-10");
        assertTrue(optionalDriver.isPresent());
        assertEquals(LocalDate.of(1990,10,5), optionalDriver.get().getBirthDate());
    }

    @Test
    void findMainDriver() {
        var mainCarDriverDocument = "123.456.789-10";
        var carDriverList = carDriverService.findMainDriver(100L);
        assert !carDriverList.isEmpty();
        assertEquals(mainCarDriverDocument, carDriverList.get(0).getDriver().getDocument());
    }
}