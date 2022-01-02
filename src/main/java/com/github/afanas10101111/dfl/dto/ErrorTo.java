package com.github.afanas10101111.dfl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorTo {
    private ErrorType error;
    private String description;

    @RequiredArgsConstructor
    @Getter
    public enum ErrorType {
        DATA_ACCESS(HttpStatus.NOT_FOUND),
        VOTING(HttpStatus.NOT_ACCEPTABLE),
        BAD_REQUEST(HttpStatus.UNPROCESSABLE_ENTITY),
        ACCESS(HttpStatus.FORBIDDEN),
        INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

        private final HttpStatus status;
    }
}
