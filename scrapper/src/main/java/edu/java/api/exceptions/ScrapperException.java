package edu.java.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class ScrapperException extends RuntimeException {
    private final String description = "Problems in Scrapper";
    private final HttpStatusCode httpStatusCode = HttpStatus.NOT_FOUND;

    public ScrapperException(String message) {
        super(message);
    }
}
