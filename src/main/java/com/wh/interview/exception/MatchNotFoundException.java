package com.wh.interview.exception;

public class MatchNotFoundException extends RuntimeException {

    public MatchNotFoundException(String messages, Object... args) {
        super(String.format(messages, args));
    }
}
