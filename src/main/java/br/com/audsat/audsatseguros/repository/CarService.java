package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarService extends JpaRepository<Car, Long> {
}
