package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.dto.ErrorTo;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.exception.TooLateToRevoteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String INTERNAL_ERROR_MESSAGE = "Error was occurred during request processing";

    @ExceptionHandler({NotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorTo> handleDataAccessException(HttpServletRequest req, Exception e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.DATA_ACCESS);
    }

    @ExceptionHandler(TooLateToRevoteException.class)
    public ResponseEntity<ErrorTo> handleTooLateToVoteException(HttpServletRequest req, TooLateToRevoteException e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.VOTING);
    }

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorTo> handleBadRequestException(HttpServletRequest req, Exception e) {
        return getErrorResponse(
                req,
                new Exception(e.getMessage().replaceFirst("^.+ codes ([^;]+); .+ default message ([^;]+)] $", "$1 $2")),
                ErrorTo.ErrorType.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorTo> handleHttpMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorTo> handleAccessDeniedException(HttpServletRequest req, AccessDeniedException e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.ACCESS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorTo> handleAnyOtherException(HttpServletRequest req, Exception e) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(e);
        log.error(
                "Detected exception: {}; during call {}",
                rootCause == null ? e.getMessage() : rootCause.getMessage(),
                req.getRequestURL());
        return getErrorResponse(req, new Exception(INTERNAL_ERROR_MESSAGE), ErrorTo.ErrorType.INTERNAL_ERROR);
    }

    private ResponseEntity<ErrorTo> getErrorResponse(HttpServletRequest req, Throwable e, ErrorTo.ErrorType errorType) {
        String message = e.getMessage();
        log.warn("Detected exception: {}; during call {}", message, req.getRequestURL());
        return ResponseEntity
                .status(errorType.getStatus())
                .body(new ErrorTo(errorType, message));
    }
}
