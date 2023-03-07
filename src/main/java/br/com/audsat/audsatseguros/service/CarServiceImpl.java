package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Car;
import br.com.audsat.audsatseguros.repository.CarRepository;

import java.util.Optional;

public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
}
