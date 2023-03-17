package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.InsuranceParams;
import br.com.audsat.audsatseguros.domain.Status;
import br.com.audsat.audsatseguros.repository.InsuranceParamsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceParamsServiceImpl implements InsuranceParamsService {

    private final InsuranceParamsRepository insuranceParamsRepository;

    @Override
    public Optional<InsuranceParams> findByStatusActive() {
        var insuranceParamsList = insuranceParamsRepository.findByStatus(Status.ACTIVE);
        if(insuranceParamsList != null && !insuranceParamsList.isEmpty()) {
            return Optional.of(insuranceParamsList.get(0));
        }
        return Optional.empty();
    }
}
