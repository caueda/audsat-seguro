package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.InsuranceParams;
import br.com.audsat.audsatseguros.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InsuranceParamsRepository extends JpaRepository<InsuranceParams, Long> {
    @Query("SELECT p FROM InsuranceParams p WHERE p.status = :status")
    List<InsuranceParams> findByStatus(@Param("status") Status status);
}
