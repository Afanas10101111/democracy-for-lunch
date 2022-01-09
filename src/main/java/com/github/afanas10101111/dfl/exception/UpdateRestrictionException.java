package com.github.afanas10101111.dfl.exception;

public class UpdateRestrictionException extends RuntimeException {
    public UpdateRestrictionException(long id) {
        super(String.format("Update is restricted for entity with id = %d", id));
    }
}
