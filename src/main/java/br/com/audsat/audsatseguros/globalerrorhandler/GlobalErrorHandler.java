package br.com.audsat.audsatseguros.globalerrorhandler;

import br.com.audsat.audsatseguros.exception.InsuranceBusinessException;
import br.com.audsat.audsatseguros.exception.InsuranceParamsNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    @ExceptionHandler(InsuranceParamsNotFoundException.class)
    public ResponseEntity<String> handleInsuranceParamsNotFoundException(InsuranceParamsNotFoundException ex) {
        log.error("Exception caught in handleInsuranceParamsNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(InsuranceBusinessException.class)
    public ResponseEntity<String> InsuranceBusinessException(InsuranceBusinessException ex) {
        log.error("Exception caught in InsuranceBusinessException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
        log.error("Exception Caught in handleRequestBodyError: {}", ex.getMessage(), ex);
        var error = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(","));
        log.error("Error is : {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
