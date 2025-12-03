package com.example.HotelService.Exceptions;

import com.example.HotelService.DTO.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ============================
    // Handle Hotel Not Found
    // ============================
    @ExceptionHandler(HotelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(HotelNotFoundException ex, WebRequest req) {

        ErrorResponse err = new ErrorResponse(
                404,
                "Not Found",
                ex.getMessage(),
                req.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    // ============================
    // Handle General Exceptions
    // ============================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, WebRequest req) {

        ex.printStackTrace(); // Debugging (optional)

        ErrorResponse err = new ErrorResponse(
                500,
                "Internal Server Error",
                ex.getMessage(),
                req.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =============================================================
    // Handle Invalid/Malformed JSON — UPDATED SIGNATURE (Spring 6)
    // =============================================================
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse err = new ErrorResponse(
                400,
                "Bad Request",
                "Malformed JSON request",
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    // ====================================================================
    // Handle Validation Errors — UPDATED SIGNATURE (Spring Boot 3)
    // ====================================================================
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(err -> (err instanceof FieldError) ?
                        ((FieldError) err).getField() + ": " + err.getDefaultMessage()
                        : err.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse err = new ErrorResponse(
                422,
                "Validation Failed",
                String.join("; ", errors),
                request.getDescription(false).replace("uri=", ""),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(err, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
