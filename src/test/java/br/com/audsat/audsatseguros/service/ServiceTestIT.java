package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Claim;
import br.com.audsat.audsatseguros.domain.Driver;
import br.com.audsat.audsatseguros.repository.InsuranceParamsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class ServiceTestIT {

    @Autowired
    private DriverService driverService;

    @Autowired
    private CarDriverService carDriverService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private InsuranceParamsRepository insuranceParamsRepository;

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

    @Test
    void findClaimByDriverId() {
        var driverIdWithOneClaim = 3L;
        var driverIdWithNoClaim = 1L;
        assert claimService.findClaimByDriverId(driverIdWithOneClaim).size() == 1;
        assert claimService.findClaimByDriverId(driverIdWithNoClaim).size() == 0;
    }

    @Test
    void findClaimByCarId() {
        var carIdWithOneClaim = 110L;
        var driverIdWithNoClaim = 1L;
        assert claimService.findClaimByCarId(carIdWithOneClaim).size() == 1;
        assert claimService.findClaimByCarId(driverIdWithNoClaim).size() == 0;
    }

    @Test
    void testInsuranceParams() {
        assert insuranceParamsRepository.findAll().size() == 1;
    }
}