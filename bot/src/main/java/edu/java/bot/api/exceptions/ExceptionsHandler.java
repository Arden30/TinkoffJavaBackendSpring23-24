package edu.java.bot.api.exceptions;

import edu.java.bot.api.dto.response.ApiErrorResponse;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
        HandlerMethodValidationException exception
    ) {
        HttpStatusCode statusCode = exception.getStatusCode();
        String description = Arrays.toString(exception.getDetailMessageArguments());
        ApiErrorResponse errorResponse =
            buildDefaultErrorResponse(statusCode, description, exception);

        return ResponseEntity.status(statusCode).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        HttpStatusCode statusCode = e.getStatusCode();
        String description = Arrays.toString(e.getDetailMessageArguments());
        ApiErrorResponse apiErrorResponse =
            buildDefaultErrorResponse(statusCode, description, e);

        return ResponseEntity.status(statusCode).body(apiErrorResponse);
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
