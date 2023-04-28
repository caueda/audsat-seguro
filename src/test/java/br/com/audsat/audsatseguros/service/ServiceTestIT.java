package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Driver;
import br.com.audsat.audsatseguros.dto.InsuranceDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class ServiceTestIT {

    @Autowired
    private DriverService driverService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CarDriverService carDriverService;

    @Autowired
    private ClaimService claimService;

    @Autowired
    private InsuranceParamsService insuranceParamsService;

    @Autowired
    private InsuranceService insuranceService;

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
        var optionalCarDriver = carDriverService.findMainDriver(100L);
        assert optionalCarDriver.isPresent();
        assertEquals(mainCarDriverDocument, optionalCarDriver.get().getDriver().getDocument());
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
        var optionalInsuranceParams = insuranceParamsService.findByStatusActive();
        assertTrue(optionalInsuranceParams.isPresent());
        assertEquals(1, optionalInsuranceParams.get().getId());
    }

    @Test
    void saveInsurance() {
        InsuranceDTO insuranceRequest = InsuranceDTO
                .builder()
                .customerId(10L)
                .carId(100L)
                .build();
        var savedInsurance = insuranceService.save(insuranceRequest);
        assertNotNull(savedInsurance.getId());
        assertThat(0.06, equalTo(savedInsurance.getQuote()));
        assertThat(4800.0, equalTo(savedInsurance.getInsuranceValue()));
    }

    @Test
    void testPathMatchingResourcePatternResolver() throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        Resource[] resources = resolver.getResources("classpath*:data.sql");
        for (Resource resource: resources) {
            System.out.println(resource);
        }
    }
}