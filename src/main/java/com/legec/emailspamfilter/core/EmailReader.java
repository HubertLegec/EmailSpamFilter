package com.legec.emailspamfilter.core;

import java.io.File;

public class EmailReader implements InputTextReader{
    private File inputFile = null;
    private long position = -1;

    public EmailReader(File inputFile) {
        this.inputFile = inputFile;
    }

    public EmailReader() {
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public char getNextCharacter(){
        return 'a';
    }

    public boolean hasNext(){
        return true;
    }
}

