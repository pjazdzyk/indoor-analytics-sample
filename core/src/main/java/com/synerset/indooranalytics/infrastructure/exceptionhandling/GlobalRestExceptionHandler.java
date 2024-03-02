package com.synerset.indooranalytics.infrastructure.exceptionhandling;

import com.synerset.brentsolver.BrentSolverException;
import com.synerset.hvacengine.common.exceptions.HvacEngineArgumentException;
import com.synerset.hvacengine.common.exceptions.HvacEngineMissingArgumentException;
import com.synerset.indooranalyticsapi.common.InvalidResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_LOG_MSG = "Invalid request, calculations are not possible. Cause: ";
    private static final String SERVICE_NAME = "Indoor Analytics";

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    protected ResponseEntity<Object> handleParsingExceptions(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_LOG_MSG, ex);
        Throwable rootCause = getRootCause(ex);
        InvalidResponse invalidResponse = new InvalidResponse(SERVICE_NAME, rootCause.getClass().getSimpleName(),
                ERROR_LOG_MSG + rootCause.getMessage(), ZonedDateTime.now());
        return handleExceptionInternal(ex, invalidResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class, IndoorAnalyticsInvalidArgumentException.class})
    protected ResponseEntity<Object> handleValidationExceptions(Exception ex, WebRequest request) {
        logger.error(ERROR_LOG_MSG, ex);
        InvalidResponse invalidResponse = new InvalidResponse(SERVICE_NAME, ex.getClass().getSimpleName(),
                ex.getMessage(), ZonedDateTime.now());
        return handleExceptionInternal(ex, invalidResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        logger.error(ERROR_LOG_MSG, ex);
        Object[] detailMessageArguments = ex.getDetailMessageArguments();
        String message;
        if (detailMessageArguments != null) {
            message = Arrays.stream(detailMessageArguments)
                    .map(obj -> (List<String>) obj)
                    .flatMap(List::stream)
                    .reduce((s1, s2) -> s1 + s2)
                    .orElse("");
        } else {
            message = "Cause could not be determined";
        }
        InvalidResponse invalidResponse = new InvalidResponse(SERVICE_NAME, ex.getClass().getSimpleName(),
                ERROR_LOG_MSG + message, ZonedDateTime.now());
        return handleExceptionInternal(ex, invalidResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {BrentSolverException.class})
    protected ResponseEntity<Object> handleSolverExceptions(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_LOG_MSG, ex);
        InvalidResponse invalidResponse = new InvalidResponse(SERVICE_NAME, ex.getClass().getSimpleName(),
                ERROR_LOG_MSG + "Solver convergence failure, reason: " + ex.getMessage(), ZonedDateTime.now());
        return handleExceptionInternal(ex, invalidResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {HvacEngineArgumentException.class, HvacEngineMissingArgumentException.class})
    protected ResponseEntity<Object> handleArgumentExceptions(RuntimeException ex, WebRequest request) {
        logger.error(ERROR_LOG_MSG, ex);
        InvalidResponse invalidResponse = new InvalidResponse(SERVICE_NAME, ex.getClass().getSimpleName(),
                ERROR_LOG_MSG + "Unphysical value caused calculation failure. " + ex.getMessage(),
                ZonedDateTime.now());
        return handleExceptionInternal(ex, invalidResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }


    private static Throwable getRootCause(Throwable e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

}