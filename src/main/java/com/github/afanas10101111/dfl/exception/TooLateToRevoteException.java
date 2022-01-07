package com.github.afanas10101111.dfl.exception;

import static com.github.afanas10101111.dfl.service.VoteService.REVOTE_CLOSING_TIME;

public class TooLateToRevoteException extends RuntimeException {
    public TooLateToRevoteException() {
        super(String.format("Too late to revote! Voting ends at %s", REVOTE_CLOSING_TIME));
    }
}
