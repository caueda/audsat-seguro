package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {
}
