package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
    List<Claim> findClaimByDriverId(Long driverId);

    List<Claim> findClaimByCarId(Long carId);
}
