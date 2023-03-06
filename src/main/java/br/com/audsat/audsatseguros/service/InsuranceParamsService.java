package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Customer;
import br.com.audsat.audsatseguros.domain.InsuranceParams;

import java.util.List;
import java.util.Optional;

public interface InsuranceParamsService {
    Optional<InsuranceParams> findByStatusActive();
}
