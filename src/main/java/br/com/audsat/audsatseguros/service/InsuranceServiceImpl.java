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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;

    private final DriverService driverService;

    private final CarDriverService carDriverService;

    private final ClaimService claimService;

    private final CustomerService customerService;

    private final InsuranceParamsService insuranceParamsService;

    private final CarService carService;

    private final InsuranceSenderService insuranceSenderService;

    public Insurance calculateInsurance(InsuranceDTO insuranceDTO) {
        return recalculateInsurance(Insurance
                .builder()
                .insuranceStatus(InsuranceStatus.CREATED)
                .build(), insuranceDTO);
    }

    public Insurance recalculateInsurance(final Insurance insurance, InsuranceDTO insuranceDTO) {
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

        var insurance = calculateInsurance(insuranceDTO);

        var createdInsurance = insuranceRepository.save(insurance);

        insuranceSenderService.sendMessage(createdInsurance);
        return createdInsurance;
    }

    @Override
    public Insurance update(Long id, InsuranceDTO insuranceDTO) {
        var insurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new InsuranceBusinessException("No Insurance with id: " + id));
        insurance.setInsuranceStatus(InsuranceStatus.UPDATED);
        recalculateInsurance(insurance, insuranceDTO);
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
