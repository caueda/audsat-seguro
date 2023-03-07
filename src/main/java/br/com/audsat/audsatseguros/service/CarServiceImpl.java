package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Car;
import br.com.audsat.audsatseguros.repository.CarService;

import java.util.Optional;

public class CarServiceImpl implements br.com.audsat.audsatseguros.service.CarService {

    private CarService carRepository;

    public CarServiceImpl(CarService carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
}
