package com.legec.tkom.core;

import com.legec.tkom.core.model.Token;

class Lexer {
    private InputTextReader inputTextReader;

    Lexer(InputTextReader inputTextReader){
        this.inputTextReader = inputTextReader;
    }

    public Token getNextToken(){
        return null;
    }
}
