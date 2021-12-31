package com.github.afanas10101111.dfl.web;

import com.github.afanas10101111.dfl.dto.ErrorTo;
import com.github.afanas10101111.dfl.exception.NotFoundException;
import com.github.afanas10101111.dfl.exception.TooLateToVoteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler{

    @ExceptionHandler({NotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorTo> handleNotFoundException(HttpServletRequest req, Exception e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.DATA_ACCESS);
    }

    @ExceptionHandler(TooLateToVoteException.class)
    public ResponseEntity<ErrorTo> handleNotFoundException(HttpServletRequest req, TooLateToVoteException e) {
        return getErrorResponse(req, e, ErrorTo.ErrorType.VOTING);
    }

    private ResponseEntity<ErrorTo> getErrorResponse(HttpServletRequest req, Throwable e, ErrorTo.ErrorType errorType) {
        String message = e.getMessage();
        log.warn("Detected exception: {}; during call {}", message, req.getRequestURL());
        return ResponseEntity
                .status(errorType.getStatus())
                .body(new ErrorTo(errorType, message));
    }
}
