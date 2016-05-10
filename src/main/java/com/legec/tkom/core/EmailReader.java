package com.legec.tkom.core;

import com.legec.tkom.core.model.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class EmailReader implements InputTextReader{
    private Position position = new Position();
    private String[] lines;
    private boolean hasNext;

    @Override
    public void setInputFile(Path inputFilePath) throws IOException {
        lines = Files.lines(inputFilePath).toArray(String[]::new);
        position.reset();
        if(lines.length > 0 && lines[0].length() > 0){
            addLinesEnds();
            hasNext = true;
        } else {
            hasNext = false;
        }
    }

    @Override
    public char getNextCharacter(){
            char toReturn = lines[position.getLine() - 1]
                    .charAt(position.getPositionInLine() - 1);
            updatePosition();
            return toReturn;
    }

    @Override
    public boolean hasNext(){
        return hasNext;
    }

    @Override
    public Position getPosition(){
        return position;
    }

    private void updatePosition(){
        int positionInLine = position.getPositionInLine();
        int lineNumber = position.getLine();
        if(positionInLine < lines[lineNumber - 1].length()){
            position.nextPositionInLine();
        } else if (lineNumber < lines.length){
            position.nextLine();
        } else {
            hasNext = false;
        }
    }

    private void addLinesEnds(){
        for(int i = 0; i < lines.length - 1; i++){
            lines[i] = lines[i] + "\n";
        }
    }
}

