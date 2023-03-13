package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.*;
import br.com.audsat.audsatseguros.dto.InsuranceDTO;
import br.com.audsat.audsatseguros.exception.InsuranceBusinessException;
import br.com.audsat.audsatseguros.exception.InsuranceParamsNotFoundException;
import br.com.audsat.audsatseguros.repository.CarService;
import br.com.audsat.audsatseguros.repository.InsuranceRepository;
import br.com.audsat.audsatseguros.service.jms.InsuranceSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceServiceImplTest {

    @Mock
    private InsuranceRepository insuranceRepository;
    @Mock
    private DriverService driverService;
    @Mock
    private CarDriverService carDriverService;
    @Mock
    private ClaimService claimService;
    @Mock
    private CustomerService customerService;
    @Mock
    private InsuranceParamsService insuranceParamsService;
    @Mock
    private CarService carService;

    @Mock
    private InsuranceSenderService insuranceSenderService;

    private InsuranceService insuranceService;

    @BeforeEach
    public void setUp() {
        this.insuranceService = new InsuranceServiceImpl(insuranceRepository,
                driverService,
                carDriverService,
                claimService,
                customerService,
                insuranceParamsService,
                carService, insuranceSenderService);
    }

    public InsuranceParams mockInsuranceParams() {
        return InsuranceParams
                .builder()
                .id(1L)
                .quote(0.06)
                .aggravatingQuote(0.02)
                .initialAge(18)
                .endAge(25)
                .status(Status.ACTIVE)
                .build();
    }

    public Car mockCar() {
        return Car.builder()
                .id(1L)
                .year("2018")
                .model("COROLLA")
                .manufacturer("TOYOTA")
                .fipeValue(80000d)
                .build();
    }

    public Driver mockDriver() {
        return Driver.builder()
                .id(1L)
                .birthDate(LocalDate.now())
                .document("999.999.999-99")
                .build();
    }

    public Customer mockCustomer() {
        return Customer.builder()
                .id(1L)
                .driver(mockDriver())
                .build();
    }

    public CarDriver mockCarDriver_mainDriver() {
        return CarDriver.builder()
                .id(1L)
                .car(mockCar())
                .driver(mockDriver())
                .mainDriver(true)
                .build();
    }

    public CarDriver mockCarDriver_Not_mainDriver() {
        return CarDriver.builder()
                .id(1L)
                .car(mockCar())
                .driver(mockDriver())
                .mainDriver(true)
                .build();
    }

    public List<Claim> mockClaims() {
        return List.of(Claim.builder()
                        .car(mockCar())
                        .driver(mockDriver())
                        .eventDate(LocalDate.now())
                        .build());
    }

    @Test
    void calculateInsurance_When_No_InsuranceParams_Found() {
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.empty());
        var exception = assertThrows(InsuranceParamsNotFoundException.class,
                ()-> insuranceService.calculateInsurance(new Insurance(), new InsuranceDTO()));
        assertThat(exception.getMessage(), equalTo("No Insurance Params found"));
    }

    @Test
    void calculateInsurance_When_No_Customer_Found() {
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(new InsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(InsuranceBusinessException.class,
                ()-> insuranceService.calculateInsurance(new Insurance(), InsuranceDTO
                        .builder()
                        .customerId(1L)
                        .build()));
        assertThat(exception.getMessage(), equalTo("Customer not found in the system."));
    }

    @Test
    void calculateInsurance_When_No_Car_Found() {
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(new InsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(new Customer()));
        when(carService.findById(anyLong())).thenReturn(Optional.empty());
        var exception = assertThrows(InsuranceBusinessException.class,
                ()-> insuranceService.calculateInsurance(new Insurance(), InsuranceDTO
                        .builder()
                        .customerId(1L)
                        .carId(1L)
                        .build()));
        assertThat(exception.getMessage(), equalTo("Car not found in the system."));
    }

    @Test
    void calculateInsurance_With_All_Aggravating() {
        var insurance = Insurance.builder().build();
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(mockInsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(carService.findById(anyLong())).thenReturn(Optional.of(mockCar()));
        when(carDriverService.findMainDriver(anyLong())).thenReturn(Optional.of(mockCarDriver_mainDriver()));
        when(driverService.getDriverAgeOnBaseDate(isA(Driver.class), isA(LocalDate.class))).thenReturn(18L);
        when(claimService.findClaimByCarId(anyLong())).thenReturn(mockClaims());
        when(claimService.findClaimByDriverId(anyLong())).thenReturn(mockClaims());

        insuranceService.calculateInsurance(insurance, insuranceDTO);
        assertThat(insurance.getQuote(), equalTo(0.06 + 0.02 + 0.02 + 0.02));
        assertThat(insurance.getInsuranceValue(), equalTo((0.06 + 0.02 + 0.02 + 0.02) * mockCar().getFipeValue() ));
    }

    @Test
    void calculateInsurance_With_No_Aggravating() {
        var insurance = Insurance.builder().build();
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(mockInsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(carService.findById(anyLong())).thenReturn(Optional.of(mockCar()));
        when(carDriverService.findMainDriver(anyLong())).thenReturn(Optional.of(mockCarDriver_Not_mainDriver()));
        when(driverService.getDriverAgeOnBaseDate(isA(Driver.class), isA(LocalDate.class))).thenReturn(30L);
        when(claimService.findClaimByCarId(anyLong())).thenReturn(Collections.EMPTY_LIST);
        when(claimService.findClaimByDriverId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        insuranceService.calculateInsurance(insurance, insuranceDTO);
        assertThat(insurance.getQuote(), equalTo(0.06 ));
        assertThat(insurance.getInsuranceValue(), equalTo(0.06  * mockCar().getFipeValue() ));
    }

    @Test
    void calculateInsurance_Documentation_Example() {
        var insurance = Insurance.builder().build();
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(mockInsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(carService.findById(anyLong())).thenReturn(Optional.of(mockCar()));
        when(carDriverService.findMainDriver(anyLong())).thenReturn(Optional.of(mockCarDriver_mainDriver()));
        when(driverService.getDriverAgeOnBaseDate(isA(Driver.class), isA(LocalDate.class))).thenReturn(25L);
        when(claimService.findClaimByCarId(anyLong())).thenReturn(Collections.EMPTY_LIST);
        when(claimService.findClaimByDriverId(anyLong())).thenReturn(mockClaims());

        insuranceService.calculateInsurance(insurance, insuranceDTO);
        assertThat(insurance.getQuote(), equalTo(0.06 + 0.02 + 0.02 ));
        assertThat(insurance.getInsuranceValue(), equalTo((0.06 + 0.02 + 0.02)  * mockCar().getFipeValue() ));
    }

    @Test
    void save() {
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(mockInsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(carService.findById(anyLong())).thenReturn(Optional.of(mockCar()));
        when(carDriverService.findMainDriver(anyLong())).thenReturn(Optional.of(mockCarDriver_mainDriver()));
        when(driverService.getDriverAgeOnBaseDate(isA(Driver.class), isA(LocalDate.class))).thenReturn(25L);
        when(claimService.findClaimByCarId(anyLong())).thenReturn(Collections.EMPTY_LIST);
        when(claimService.findClaimByDriverId(anyLong())).thenReturn(mockClaims());

        when(insuranceRepository.save(isA(Insurance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var savedInsurance = insuranceService.save(insuranceDTO);

        assertThat(savedInsurance.getInsuranceStatus(), equalTo(InsuranceStatus.CREATED));

        Mockito.verify(insuranceRepository).save(isA(Insurance.class));
    }

    @Test
    void update_InsuranceId_notFound() {
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.empty());

        var exception = assertThrows(InsuranceBusinessException.class,
                () -> insuranceService.update(1L, insuranceDTO));
        assert  exception.getMessage().equals("No Insurance with id: 1");
    }

    @Test
    void update() {
        var insuranceDTO = InsuranceDTO.builder()
                .carId(1L)
                .customerId(1L)
                .active(true)
                .build();
        when(insuranceParamsService.findByStatusActive()).thenReturn(Optional.of(mockInsuranceParams()));
        when(customerService.findById(anyLong())).thenReturn(Optional.of(mockCustomer()));
        when(carService.findById(anyLong())).thenReturn(Optional.of(mockCar()));
        when(carDriverService.findMainDriver(anyLong())).thenReturn(Optional.of(mockCarDriver_mainDriver()));
        when(driverService.getDriverAgeOnBaseDate(isA(Driver.class), isA(LocalDate.class))).thenReturn(25L);
        when(claimService.findClaimByCarId(anyLong())).thenReturn(Collections.EMPTY_LIST);
        when(claimService.findClaimByDriverId(anyLong())).thenReturn(mockClaims());
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.of(Insurance.builder()
                .id(1L)
                .customer(mockCustomer())
                .car(mockCar())
                .active(true)
                .build()));
        when(insuranceRepository.save(isA(Insurance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedInsurance = insuranceService.update(1L, insuranceDTO);

        assertThat(updatedInsurance.getInsuranceStatus(), equalTo(InsuranceStatus.UPDATED));

        Mockito.verify(insuranceRepository).save(isA(Insurance.class));
    }

    @Test
    void deleteById() {
        insuranceService.deleteById(1L);
        Mockito.verify(insuranceRepository).deleteById(anyLong());
    }

    @Test
    void findById() {
        when(insuranceRepository.findById(anyLong())).thenReturn(Optional.of(new Insurance()));
        var insurance = insuranceService.findById(1L);
        assertNotNull(insurance);
    }
}