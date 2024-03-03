package edu.java.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class DoubleLinkException extends ScrapperException {
    private final String description = "This link is already being tracked";
    private final HttpStatusCode httpStatusCode = HttpStatus.CONFLICT;

    public DoubleLinkException(String message) {
        super(message);
    }
}
