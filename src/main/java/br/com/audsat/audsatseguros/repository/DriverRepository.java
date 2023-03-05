package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByDocument(String document);
}
