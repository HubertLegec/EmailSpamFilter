package com.legec.tkom.core.exception;


import com.legec.tkom.core.model.TokenPosition;

public class UnexpectedCharacterException extends RuntimeException {
    private String found;
    private String expected;
    private TokenPosition position;

    public UnexpectedCharacterException(String found, String expected, TokenPosition position) {
        this.found = found;
        this.expected = expected;
        this.position = position;
    }

    public String getFound() {
        return found;
    }

    public String getExpected() {
        return expected;
    }

    public TokenPosition getPosition() {
        return position;
    }
}
