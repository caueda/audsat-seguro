package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Insurance;
import br.com.audsat.audsatseguros.exception.InsuranceBusinessException;
import br.com.audsat.audsatseguros.exception.InsuranceParamsNotFoundException;
import br.com.audsat.audsatseguros.repository.InsuranceRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private InsuranceRepository insuranceRepository;

    private DriverService driverService;

    private CarDriverService carDriverService;

    private ClaimService claimService;

    private CustomerService customerService;

    private InsuranceParamsService insuranceParamsService;

    public InsuranceServiceImpl(InsuranceRepository insuranceRepository,
                                DriverService driverService,
                                CarDriverService carDriverService,
                                ClaimService claimService,
                                CustomerService customerService,
                                InsuranceParamsService insuranceParamsService) {
        this.insuranceRepository = insuranceRepository;
        this.driverService = driverService;
        this.carDriverService = carDriverService;
        this.claimService = claimService;
        this.customerService = customerService;
        this.insuranceParamsService = insuranceParamsService;
    }

    @Override
    public Insurance save(@NotNull Insurance insurance) {

        var insuranceParams = insuranceParamsService
                .findByStatusActive()
                .orElseThrow(() -> new InsuranceParamsNotFoundException("No Insurance Params found"));

        Double quote = insuranceParams.getQuote();

        var carDriver = carDriverService.findMainDriver(insurance.getCar().getId())
                .orElseThrow(() -> new InsuranceBusinessException("No main driver found for this car"));

        var car = carDriver.getCar();
        var driver = carDriver.getDriver();

        var mainDriverAge = driverService
                .getDriverAgeOnBaseDate(driver, LocalDate.now());

        if(mainDriverAge >= insuranceParams.getInitialAge() && mainDriverAge <= insuranceParams.getEndAge()) {
            quote += insuranceParams.getAggravatingQuote();
        }

        var claimsByCar = claimService.findClaimByCarId(car.getId());

        if(!claimsByCar.isEmpty()) {
            quote += insuranceParams.getAggravatingQuote();
        }
        var claimsByDriver = claimService.findClaimByDriverId(driver.getId());

        if(!claimsByDriver.isEmpty()) {
            quote += insuranceParams.getAggravatingQuote();
        }

        insurance.setQuote(quote);

        return insuranceRepository.save(insurance);
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
