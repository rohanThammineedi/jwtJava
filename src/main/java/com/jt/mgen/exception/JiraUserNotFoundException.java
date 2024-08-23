package com.jt.mgen.exception;

public class JiraUserNotFoundException extends RuntimeException {
    public JiraUserNotFoundException(String message) {
        super(message);
    }
}
