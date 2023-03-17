package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.CarDriver;
import br.com.audsat.audsatseguros.repository.CarDriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarDriverServiceImpl implements CarDriverService {

    private final CarDriverRepository carDriverRepository;

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
