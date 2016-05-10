package com.legec.tkom.core.model;

public class Token {
    private TokenType tokenType;
    private String value = null;
    private int line;
    private int position;

    public Token(TokenType tokenType, int line, int position) {
        this.tokenType = tokenType;
        this.line = line;
        this.position = position;
    }

    public Token(String value, int line, int position) {
        this.value = value;
        this.tokenType = TokenType.VALUE;
        this.line = line;
        this.position = position;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }
}
