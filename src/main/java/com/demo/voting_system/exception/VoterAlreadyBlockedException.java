package com.demo.voting_system.exception;


public class VoterAlreadyBlockedException extends VotingSystemException {
    public VoterAlreadyBlockedException(String message) {
        super(message);
    }
}
