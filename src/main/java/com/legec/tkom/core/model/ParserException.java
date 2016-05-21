package com.legec.tkom.core.model;

public class ParserException extends RuntimeException {
    private TokenPosition position;

    public ParserException(ExceptionMessage message, TokenPosition position) {
        super(message.getMessage());
        this.position = position;
    }

    public TokenPosition getPosition() {
        return position;
    }
}
