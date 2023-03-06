package br.com.audsat.audsatseguros.exception;

public class InsuranceParamsNotFoundException extends RuntimeException {
    public InsuranceParamsNotFoundException(String message) {
        super(message);
    }

    public InsuranceParamsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
