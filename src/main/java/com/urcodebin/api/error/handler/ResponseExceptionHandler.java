package com.urcodebin.api.error.handler;

import com.urcodebin.api.error.exception.PasteNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgument(final RuntimeException exception, final WebRequest request) {
        return baseExceptionHandler(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {PasteNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(final RuntimeException exception, final WebRequest request) {
        return baseExceptionHandler(exception, request, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> baseExceptionHandler(final RuntimeException exception, final WebRequest request,
                                                        final HttpStatus status) {
        ErrorResponse errorBody = createErrorResponse(exception, status);
        return handleExceptionInternal(exception, errorBody, new HttpHeaders(),
                status, request);
    }

    private ErrorResponse createErrorResponse(RuntimeException exception, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(status.value());
        errorResponse.setError(status.getReasonPhrase());
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }
}
