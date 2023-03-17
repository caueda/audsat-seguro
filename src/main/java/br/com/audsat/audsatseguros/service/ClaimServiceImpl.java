package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Claim;
import br.com.audsat.audsatseguros.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClaimServiceImpl implements ClaimService {

    private final ClaimRepository claimRepository;

    @Override
    public List<Claim> findClaimByDriverId(Long driverId) {
        return claimRepository.findClaimByDriverId(driverId);
    }

    @Override
    public List<Claim> findClaimByCarId(Long carId) {
        return claimRepository.findClaimByCarId(carId);
    }
}
