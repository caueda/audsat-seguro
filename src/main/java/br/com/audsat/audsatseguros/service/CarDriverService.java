package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.CarDriver;

import java.util.List;

public interface CarDriverService {
    List<CarDriver> findMainDriver(Long carId);
}
