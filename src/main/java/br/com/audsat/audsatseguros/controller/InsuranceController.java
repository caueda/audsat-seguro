package br.com.audsat.audsatseguros.controller;

import br.com.audsat.audsatseguros.domain.Insurance;
import br.com.audsat.audsatseguros.service.InsuranceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/insurance/budget")
public class InsuranceController {

    private InsuranceService insuranceService;

    public InsuranceController(InsuranceService insuranceService) {
        this.insuranceService = insuranceService;
    }

    @GetMapping("/{insuranceId}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable Long insuranceId) {
        return insuranceService.findById(insuranceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{insuranceId}")
    public void deleteById(@PathVariable Long insuranceId) {
        insuranceService.deleteById(insuranceId);
    }
}
