package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Car;

import java.util.Optional;

public interface CarService {
    Optional<Car> findById(Long id);
}
