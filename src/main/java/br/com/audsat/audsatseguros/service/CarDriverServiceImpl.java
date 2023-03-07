package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.CarDriver;
import br.com.audsat.audsatseguros.repository.CarDriverRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarDriverServiceImpl implements CarDriverService {

    private CarDriverRepository carDriverRepository;

    public CarDriverServiceImpl(CarDriverRepository carDriverRepository) {
        this.carDriverRepository = carDriverRepository;
    }

    @Override
    public Optional<CarDriver> findMainDriver(Long carId) {
        if (carId == null) {
            return Optional.empty();
        }
        return carDriverRepository.findCarDriverByCarIdAndMainDriverIsTrue(carId)
                .stream().findFirst();
    }

    @Override
    public CarDriver save(CarDriver carDriver) {
        return carDriverRepository.save(carDriver);
    }
}
