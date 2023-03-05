package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.CarDriver;
import br.com.audsat.audsatseguros.repository.CarDriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarDriverServiceImpl implements CarDriverService {

    private CarDriverRepository carDriverRepository;

    public CarDriverServiceImpl(CarDriverRepository carDriverRepository) {
        this.carDriverRepository = carDriverRepository;
    }

    @Override
    public List<CarDriver> findMainDriver(Long carId) {
        return carDriverRepository.findCarDriverByCarIdAndMainDriverIsTrue(carId);
    }
}
