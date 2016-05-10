package com.legec.tkom.core;

import java.io.IOException;
import java.nio.file.Path;

public class SpamDetector {
    private InputTextReader inputTextReader;
    private Lexer lexer;

    public SpamDetector(){
        inputTextReader = new EmailReader();
        lexer = new Lexer(inputTextReader);
    }

    public void init(Path inputFilePath) throws IOException{
        inputTextReader.setInputFile(inputFilePath);
    }
}
