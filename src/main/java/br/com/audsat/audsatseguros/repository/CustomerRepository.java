package br.com.audsat.audsatseguros.repository;

import br.com.audsat.audsatseguros.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
