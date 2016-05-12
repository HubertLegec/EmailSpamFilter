package com.legec.tkom.core;

import com.legec.tkom.core.model.EmailType;
import com.legec.tkom.core.model.ParserException;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class SpamDetector {
    private InputTextReader inputTextReader;
    private Parser parser;
    private Filter filter;
    private ParserException exception;
    private EmailType emailType;

    public SpamDetector() {
        inputTextReader = new EmailReader();
        Lexer lexer = new Lexer(inputTextReader);
        parser = new Parser(lexer);
    }

    public void init(Path inputFilePath) throws IOException {
        inputTextReader.setInputFile(inputFilePath);
    }

    public boolean process() {
        try {
            parser.parse();
            filter = new Filter(parser.getModel());
            emailType = filter.processEmail();
            return true;
        } catch (ParserException e) {
            exception = e;
            return false;
        }
    }

    public Pair<EmailType, List<String>> getResult() {
        return new Pair<>(emailType, filter.getSuspiciousElements());
    }

    public ParserException getException() {
        return exception;
    }
}
