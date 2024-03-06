package edu.java.api.exceptions;

import edu.java.api.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        String description = Arrays.toString(e.getDetailMessageArguments());
        ApiErrorResponse apiErrorResponse =
            buildDefaultErrorResponse(statusCode, description, e);

        return ResponseEntity.status(statusCode).body(apiErrorResponse);
    }

    @ExceptionHandler(ScrapperException.class)
    public ResponseEntity<ApiErrorResponse> handleScrapperException(ScrapperException exception) {
        ApiErrorResponse apiErrorResponse = buildDefaultErrorResponse(exception.getHttpStatusCode(),
            exception.getDescription(), exception
        );

        return ResponseEntity.status(exception.getHttpStatusCode()).body(apiErrorResponse);
    }

    private ApiErrorResponse buildDefaultErrorResponse(
        HttpStatusCode statusCode,
        String description,
        Exception exception
    ) {
        String exceptionName = exception.getClass().getSimpleName();
        String exceptionMessage = exception.getMessage();
        List<String> stacktrace = Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList();

        return new ApiErrorResponse(
            description,
            statusCode.toString(),
            exceptionName,
            exceptionMessage,
            stacktrace
        );
    }
}
