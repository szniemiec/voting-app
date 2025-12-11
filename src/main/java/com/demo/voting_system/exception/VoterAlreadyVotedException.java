package com.demo.voting_system.exception;


public class VoterAlreadyVotedException extends VotingSystemException {
    public VoterAlreadyVotedException(String message) {
        super(message);
    }
}
