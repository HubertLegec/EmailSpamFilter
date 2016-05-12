package com.legec.tkom.core.model;

public class ParserException extends RuntimeException {
    private TokenPosition position;

    public ParserException(String message, TokenPosition position) {
        super(message);
        this.position = position;
    }

    public TokenPosition getPosition() {
        return position;
    }
}
