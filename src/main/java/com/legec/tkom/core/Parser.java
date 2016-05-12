package com.legec.tkom.core;

import com.legec.tkom.core.model.EmailModel;
import com.legec.tkom.core.model.ParserException;
import com.legec.tkom.core.model.Token;

class Parser {
    private Lexer lexer;
    private EmailModel model = new EmailModel();


    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    void parse() throws ParserException {
        Token token = lexer.getNextToken();
        while (token != null){
            //TODO

            token = lexer.getNextToken();
        }
    }

    EmailModel getModel() {
        return model;
    }
}
