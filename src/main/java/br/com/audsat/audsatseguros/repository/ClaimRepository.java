package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimRepository extends JpaRepository<Claim, Long> {
}
