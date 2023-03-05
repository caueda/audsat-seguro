package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Claim;

import java.util.List;

public interface ClaimService {
    List<Claim> findClaimByDriverId(Long driverId);

    List<Claim> findClaimByCarId(Long carId);
}
