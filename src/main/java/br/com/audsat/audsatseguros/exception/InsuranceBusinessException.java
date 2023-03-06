package br.com.audsat.audsatseguros.exception;

public class InsuranceBusinessException extends RuntimeException {
    public InsuranceBusinessException(String message) {
        super(message);
    }

    public InsuranceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}