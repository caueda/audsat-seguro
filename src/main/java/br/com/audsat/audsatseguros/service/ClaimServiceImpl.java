package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Claim;
import br.com.audsat.audsatseguros.repository.ClaimRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {

    private ClaimRepository claimRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    @Override
    public List<Claim> findClaimByDriverId(Long driverId) {
        return claimRepository.findClaimByDriverId(driverId);
    }

    @Override
    public List<Claim> findClaimByCarId(Long carId) {
        return claimRepository.findClaimByCarId(carId);
    }
}
