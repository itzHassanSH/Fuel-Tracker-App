package com.fueltracker.advice;

import com.fueltracker.advice.exceptions.*;
import lombok.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.BindException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RateLimitExceeded.class)
    public ResponseEntity<@NonNull ErrorResponse> handleRateLimit(RateLimitExceeded exc) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new com.fueltracker.advice.ErrorResponse("RATE_LIMIT_EXCEEDED", exc.getMessage()));
    }

    @ExceptionHandler(LocationNotFound.class)
    public ResponseEntity<@NonNull ErrorResponse> handleLocationNotFound(LocationNotFound exc) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                // 422 — if the request was well-formed and understood, but the semantic content couldn't be resolved.
                .body(new ErrorResponse("LOCATION_NOT_PROCESSED", exc.getMessage()));
    }

    // pretty much not needed, since I do try and catch within scheduler method
    @ExceptionHandler(StationNotFound.class)
    public ResponseEntity<@NonNull ErrorResponse> handleStationNotFound(StationNotFound exc) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // 404 = "this specific resource, identified by ID/slug/path segment, doesn't exist."
                .body(new ErrorResponse("STATION_NOT_FOUND", exc.getMessage()));
    }

    @ExceptionHandler(TankerKoenigApiException.class)
    public ResponseEntity<@NonNull ErrorResponse> handleTankerKoenigApi(TankerKoenigApiException exc) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY) // 502 — upstream API failed/misbehaved
                .body(new ErrorResponse("UPSTREAM_API_ERROR", "Fuel price service is currently unavailable"));
    }
    // we don't pass the raw exception message to client here, since it may contain sensitive information such as API key

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        String message = (body instanceof ProblemDetail pd && pd.getDetail() != null)
                ? pd.getDetail()
                : "Request failed";
        HttpStatus status = HttpStatus.resolve(statusCode.value());
        String code = status != null ? status.name() : "ERROR";   // e.g. "BAD_REQUEST"

        return ResponseEntity.status(statusCode).headers(headers)
                .body(new ErrorResponse(code, message));
    }

    // Bean Validation failures on a @Valid @ModelAttribute typically throw BindException - MethodArgumentNotValidException extends BindException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(status)
                .body(new ErrorResponse("VALIDATION_FAILED", message));
    }

    // Last resort: log it, never leak internals
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull ErrorResponse> handleUnexpected(Exception exc) {
        log.error("Unhandled Exception", exc);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500 — internal server error - handles all exceptions not already explicitly covered
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR", "Unexcepted Error"));
    }
}
