package edu.java.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public class NoSuchChatException extends ScrapperException {
    private final String description = "Chat is not registered";
    private final HttpStatusCode httpStatusCode = HttpStatus.NOT_FOUND;

    public NoSuchChatException(String message) {
        super(message);
    }
}
