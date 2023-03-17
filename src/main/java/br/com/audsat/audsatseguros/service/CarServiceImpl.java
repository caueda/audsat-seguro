package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Car;
import br.com.audsat.audsatseguros.repository.CarRepository;
import br.com.audsat.audsatseguros.repository.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements br.com.audsat.audsatseguros.service.CarService {

    private final CarRepository carRepository;

    @Override
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
}
