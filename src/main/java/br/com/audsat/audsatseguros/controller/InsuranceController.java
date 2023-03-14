package br.com.audsat.audsatseguros.controller;

import br.com.audsat.audsatseguros.domain.Insurance;
import br.com.audsat.audsatseguros.dto.InsuranceDTO;
import br.com.audsat.audsatseguros.service.InsuranceService;
import jakarta.jms.JMSException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long insuranceId) {
        insuranceService.deleteById(insuranceId);
    }

    @PostMapping
    public ResponseEntity<Insurance> saveInsurance(@RequestBody @Valid InsuranceDTO insuranceRequest) {
        var savedInsurance = insuranceService.save(insuranceRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedInsurance.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedInsurance);
    }

    @PutMapping("/{insuranceId}")
    public ResponseEntity<Insurance> saveInsurance(@PathVariable Long insuranceId, @RequestBody @Valid InsuranceDTO insuranceRequest) throws JMSException {
        var savedInsurance = insuranceService.update(insuranceId, insuranceRequest);
        return ResponseEntity.ok(savedInsurance);
    }
}
