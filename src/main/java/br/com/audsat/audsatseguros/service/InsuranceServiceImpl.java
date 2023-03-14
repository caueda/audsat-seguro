package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Insurance;
import br.com.audsat.audsatseguros.domain.InsuranceStatus;
import br.com.audsat.audsatseguros.dto.InsuranceDTO;
import br.com.audsat.audsatseguros.exception.InsuranceBusinessException;
import br.com.audsat.audsatseguros.exception.InsuranceParamsNotFoundException;
import br.com.audsat.audsatseguros.repository.CarService;
import br.com.audsat.audsatseguros.repository.InsuranceRepository;
import br.com.audsat.audsatseguros.service.jms.InsuranceSenderService;
import jakarta.jms.JMSException;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class InsuranceServiceImpl implements InsuranceService {

    private InsuranceRepository insuranceRepository;

    private DriverService driverService;

    private CarDriverService carDriverService;

    private ClaimService claimService;

    private CustomerService customerService;

    private InsuranceParamsService insuranceParamsService;

    private CarService carService;

    private InsuranceSenderService insuranceSenderService;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository,
                                DriverService driverService,
                                CarDriverService carDriverService,
                                ClaimService claimService,
                                CustomerService customerService,
                                InsuranceParamsService insuranceParamsService,
                                CarService carService, InsuranceSenderService insuranceSenderService) {
        this.insuranceRepository = insuranceRepository;
        this.driverService = driverService;
        this.carDriverService = carDriverService;
        this.claimService = claimService;
        this.customerService = customerService;
        this.insuranceParamsService = insuranceParamsService;
        this.carService = carService;
        this.insuranceSenderService = insuranceSenderService;
    }

    public Insurance calculateInsurance(final Insurance insurance, InsuranceDTO insuranceDTO) {
        var insuranceParams = insuranceParamsService
                .findByStatusActive()
                .orElseThrow(() -> new InsuranceParamsNotFoundException("No Insurance Params found"));

        Double quote = insuranceParams.getQuote();

        var customer = customerService.findById(insuranceDTO.getCustomerId())
                .orElseThrow(() -> new InsuranceBusinessException("Customer not found in the system."));

        var car = carService.findById(insuranceDTO.getCarId())
                .orElseThrow(() -> new InsuranceBusinessException("Car not found in the system."));

        var isMainDriver = carDriverService.findMainDriver(insuranceDTO.getCarId())
                .map(carDriver -> carDriver.getDriver().getId().equals(customer.getDriver().getId()) && carDriver.isMainDriver())
                .orElse(false);

        var mainDriverAge = driverService
                .getDriverAgeOnBaseDate(customer.getDriver(), LocalDate.now());

        if(mainDriverAge >= insuranceParams.getInitialAge() && mainDriverAge <= insuranceParams.getEndAge() && isMainDriver) {
            quote += insuranceParams.getAggravatingQuote();
        }

        var claimsByCar = claimService.findClaimByCarId(insuranceDTO.getCarId());

        if(!claimsByCar.isEmpty()) {
            quote += insuranceParams.getAggravatingQuote();
        }

        var claimsByDriver = claimService.findClaimByDriverId(customer.getDriver().getId());

        if(!claimsByDriver.isEmpty()) {
            quote += insuranceParams.getAggravatingQuote();
        }

        insurance.setQuote(quote);
        insurance.setInsuranceValue(quote * car.getFipeValue());
        insurance.setCar(car);
        insurance.setCustomer(customer);
        insurance.setActive(insuranceDTO.isActive());
        return insurance;
    }

    @Override
    public Insurance save(@NotNull InsuranceDTO insuranceDTO) {

        var insurance = calculateInsurance(Insurance
                .builder()
                .insuranceStatus(InsuranceStatus.CREATED)
                .build(), insuranceDTO);

        var createdInsurance = insuranceRepository.save(insurance);

        insuranceSenderService.sendMessage(createdInsurance);
        return createdInsurance;
    }

    @Override
    public Insurance update(Long id, InsuranceDTO insuranceDTO) {
        var insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceBusinessException("No Insurance with id: " + id));
        insurance.setInsuranceStatus(InsuranceStatus.UPDATED);
        calculateInsurance(insurance, insuranceDTO);
        log.info("Updating insurance id: {}", id);
        var updatedInsurance = insuranceRepository.save(insurance);
        try {
            insuranceSenderService.sendAndReceiveMessage(updatedInsurance);
        } catch (JMSException e) {
            throw new InsuranceBusinessException(e.getMessage());
        }
        return updatedInsurance;
    }

    @Override
    public void deleteById(Long id) {
        insuranceRepository.deleteById(id);
    }

    @Override
    public Optional<Insurance> findById(Long id) {
        return insuranceRepository.findById(id);
    }

}
