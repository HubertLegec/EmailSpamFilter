package com.legec.tkom.core.model;

public class Position {
    private int line = 0;
    private int positionInLine = 0;

    public Position() {
    }

    public int getLine() {
        return line;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public void nextLine(){
        line++;
        positionInLine = 0;
    }

    public void nextPositionInLine(){
        positionInLine++;
    }

    public void reset() {
        line = 0;
        positionInLine = 0;
    }

    public boolean isLineBegin(){
        return positionInLine == 0;
    }
}
