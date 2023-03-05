package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.CarDriver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarDriverRepository extends JpaRepository<CarDriver, Long> {
    List<CarDriver> findCarDriverByCarIdAndMainDriverIsTrue(Long carId);
}
