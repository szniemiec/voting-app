package com.demo.voting_system.exception;


public class ElectionNotActiveException extends VotingSystemException {
    public ElectionNotActiveException(String message) {
        super(message);
    }
}
