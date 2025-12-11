package com.demo.voting_system.exception;

public class VotingSystemException extends RuntimeException {
    public VotingSystemException(String message) {
        super(message);
    }

    public VotingSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
