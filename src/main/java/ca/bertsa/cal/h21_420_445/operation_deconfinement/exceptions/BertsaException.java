package ca.bertsa.cal.h21_420_445.operation_deconfinement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BertsaException extends RuntimeException {
    public BertsaException(String exception) {
        super(exception);
    }
}
