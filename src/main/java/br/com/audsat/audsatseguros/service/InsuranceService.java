package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Insurance;
import br.com.audsat.audsatseguros.dto.InsuranceDTO;

import java.util.Optional;

public interface InsuranceService {
    Insurance save(InsuranceDTO insuranceDTO);

    Insurance update(Long id, InsuranceDTO insuranceDTO);

    void deleteById(Long id);

    Optional<Insurance> findById(Long id);

    public Insurance calculateInsurance(final Insurance insurance, InsuranceDTO insuranceDTO);
}
