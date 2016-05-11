package com.legec.tkom.core.model;

public class TokenPosition {
    private int line;
    private int positionInLine;

    public TokenPosition(int line, int positionInLine) {
        this.line = line;
        this.positionInLine = positionInLine;
    }

    public int getLine() {
        return line;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof TokenPosition))return false;
        TokenPosition otherPosition = (TokenPosition) other;
        return line == otherPosition.line && positionInLine == otherPosition.positionInLine;
    }
}
