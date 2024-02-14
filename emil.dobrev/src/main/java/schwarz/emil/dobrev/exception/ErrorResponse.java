package schwarz.emil.dobrev.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        String message,
        int status,
        String error
) {
    public ErrorResponse(Exception exception, HttpStatus status) {
        this(Instant.now(),
                exception.getMessage(),
                status.value(),
                status.getReasonPhrase());
    }
}
