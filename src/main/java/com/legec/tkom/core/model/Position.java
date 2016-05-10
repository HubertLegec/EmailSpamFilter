package com.legec.tkom.core.model;

public class Position {
    private int lastLine = 1;
    private int lastPositionInLine = 1;
    private int line = 1;
    private int positionInLine = 1;

    public Position() {
    }

    public int getLine() {
        return line;
    }

    public int getPositionInLine() {
        return positionInLine;
    }

    public int getLastLine() {
        return lastLine;
    }

    public int getLastPositionInLine() {
        return lastPositionInLine;
    }

    public void nextLine(){
        lastLine = line;
        lastPositionInLine = positionInLine;
        line++;
        positionInLine = 1;
    }

    public void nextPositionInLine(){
        lastPositionInLine = positionInLine;
        positionInLine++;
    }

    public void reset() {
        line = 1;
        positionInLine = 1;
    }
}
