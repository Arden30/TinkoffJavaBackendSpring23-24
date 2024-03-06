package edu.java.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class NoSuchLinkException extends ScrapperException {
    private final String description = "Link is not registered";
    private final HttpStatusCode httpStatusCode = HttpStatus.NOT_FOUND;

    public NoSuchLinkException(String message) {
        super(message);
    }
}
