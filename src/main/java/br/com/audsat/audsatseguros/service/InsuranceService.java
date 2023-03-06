package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Insurance;

import java.util.Optional;

public interface InsuranceService {
    Insurance save(Insurance insurance);

    void deleteById(Long id);

    Optional<Insurance> findById(Long id);
}
