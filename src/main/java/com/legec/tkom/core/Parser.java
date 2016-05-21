package com.legec.tkom.core;

import com.legec.tkom.core.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.legec.tkom.core.model.ExceptionMessage.*;
import static com.legec.tkom.core.model.TokenType.*;

class Parser {

    private Lexer lexer;
    private EmailModel model = new EmailModel();
    private Token token = null;


    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    void parse() throws ParserException {
        nextToken();
        parseHeader();
        parseBody();
    }

    private void parseHeader() throws ParserException {
        checkIfPartExist(HEADER_DOES_NOT_EXIST);
        boolean validRow = true;
        while (token != null && validRow){
            parseHeaderRow();
            if(token != null) {
                validRow = token.isHeaderRowKey();
            }
        }
    }

    private void parseBody() {
        checkIfPartExist(BODY_DOES_NOT_EXIST);
        while (token != null) {
            //TODO
            token = lexer.getNextToken();
        }
    }

    private void parseHeaderRow() {
        if(!token.isHeaderRowKey()){
            throw new ParserException(HEADER_KEY_EXPECTED, token.getPosition());
        }
        HeaderKey key = HeaderKey.getFromTokenType(token.getTokenType());
        goToValue();
        List<String> values = new ArrayList<>();
        boolean wasIndentationOrSpace;
        do{
            readHeaderRowValue(key, values);
            if(token.getTokenType() == NEW_LINE){
                nextToken();
                if(token == null){
                    wasIndentationOrSpace = false;
                    continue;
                }
            }

            if(token.getTokenType() == SPACE || token.getTokenType() == INDENTATION){
                wasIndentationOrSpace = true;
                nextToken();
            } else {
                wasIndentationOrSpace = false;
            }
        } while (wasIndentationOrSpace);
        model.getEmailHeader().addHeader(key, values);
    }

    private void readHeaderRowValue(HeaderKey key, List<String> values){
        StringBuilder builder = new StringBuilder();
        while(token.getTokenType() != NEW_LINE && token.getTokenType() != SEMICOLON){
            if(token.getTokenType() == SPACE){
                builder.append(" ");
            } else if(key == HeaderKey.CONTENT_TYPE ||
                    key == HeaderKey.CONTENT_TRANSFER_ENCODING ||
                    key == HeaderKey.CONTENT_DISPOSITION){
                addKeyValues(builder, key);
            } else if(token.getTokenType() == STRING) {
                builder.append(token.getValue());
            } else {
                throw new ParserException(UNEXPECTED_HEADER_ROW_VALUE, token.getPosition());
            }
            nextToken();
        }
        if(token.getTokenType() == SEMICOLON){
            nextToken();
        }
        values.add(builder.toString());
    }

    private void addKeyValues(StringBuilder builder, HeaderKey key) {
        if(Mappings.HEADER_KEY_VALUES.get(key).contains(token.getTokenType())){
            builder.append(token.getWrappedValue());
        } else {
            throw new ParserException(UNEXPECTED_HEADER_ROW_VALUE, token.getPosition());
        }
    }

    private void checkIfPartExist(ExceptionMessage message) throws ParserException {
        if(token == null){
            throw new ParserException(message, lexer.getTokenPosition());
        }
    }

    private void goToValue(){
        nextToken();
        while(token.getTokenType() == COLON ||
                token.getTokenType() == SPACE ||
                token.getTokenType() == INDENTATION){
            nextToken();
        }
    }

    private void nextToken(){
        token = lexer.getNextToken();
    }

    EmailModel getModel() {
        return model;
    }
}
