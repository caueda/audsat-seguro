package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.CarDriver;

import java.util.Optional;

public interface CarDriverService {
    Optional<CarDriver> findMainDriver(Long carId);
}
