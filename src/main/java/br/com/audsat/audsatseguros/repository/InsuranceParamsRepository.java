package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.InsuranceParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceParamsRepository extends JpaRepository<InsuranceParams, Long> {
}
