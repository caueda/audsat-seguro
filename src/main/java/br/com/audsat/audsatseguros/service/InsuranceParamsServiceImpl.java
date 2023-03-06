package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.InsuranceParams;
import br.com.audsat.audsatseguros.domain.Status;
import br.com.audsat.audsatseguros.repository.InsuranceParamsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InsuranceParamsServiceImpl implements InsuranceParamsService {

    private InsuranceParamsRepository insuranceParamsRepository;

    public InsuranceParamsServiceImpl(InsuranceParamsRepository insuranceParamsRepository) {
        this.insuranceParamsRepository = insuranceParamsRepository;
    }

    @Override
    public Optional<InsuranceParams> findByStatusActive() {
        var insuranceParamsList = insuranceParamsRepository.findByStatus(Status.ACTIVE);
        if(insuranceParamsList != null && !insuranceParamsList.isEmpty()) {
            return Optional.of(insuranceParamsList.get(0));
        }
        return Optional.empty();
    }
}
