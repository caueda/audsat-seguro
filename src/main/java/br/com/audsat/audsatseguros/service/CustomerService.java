package br.com.audsat.audsatseguros.service;

import br.com.audsat.audsatseguros.domain.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findById(Long id);
}
