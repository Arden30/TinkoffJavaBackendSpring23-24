package edu.java.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class DoubleRegistrationException extends ScrapperException {
    private final String description = "This chat was already registered";
    private final HttpStatusCode httpStatusCode = HttpStatus.CONFLICT;

    public DoubleRegistrationException(String message) {
        super(message);
    }
}
