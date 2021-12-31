package com.github.afanas10101111.dfl.exception;

import static com.github.afanas10101111.dfl.service.VoteService.REVOTE_CLOSING_TIME;

public class TooLateToVoteException extends RuntimeException {
    public TooLateToVoteException() {
        super(String.format("Too late to vote! Voting ends at %s", REVOTE_CLOSING_TIME));
    }
}
