package com.legec.tkom.core;

import com.legec.tkom.core.model.Mappings;
import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenPosition;
import com.legec.tkom.core.model.TokenType;
import javafx.util.Pair;

import java.util.Optional;

import static com.legec.tkom.core.Utils.*;
import static com.legec.tkom.core.model.TokenType.*;

class Lexer {
    private InputTextReader inputTextReader;
    private TokenPosition currentTokenPosition;

    Lexer(InputTextReader inputTextReader){
        this.inputTextReader = inputTextReader;
    }

    Token getNextToken() {
        if(!inputTextReader.hasNext()){
            return null;
        } else {
            char processedChar = inputTextReader.seeNextCharacter();
            currentTokenPosition = getTokenPosition();
            if (inputTextReader.getPosition().isLineBegin() && isWhiteChar(processedChar)){
                removeWhiteSpace();
                return new Token(INDENTATION, currentTokenPosition);
            } else if(processedChar == '\n') {
                inputTextReader.getNextCharacter();
                return new Token(NEW_LINE, currentTokenPosition);
            } else if(processedChar == ';') {
                inputTextReader.getNextCharacter();
                return new Token(SEMICOLON, currentTokenPosition);
            } else if(processedChar == ':') {
                inputTextReader.getNextCharacter();
                return new Token(COLON, currentTokenPosition);
            } else if(processedChar == ' ') {
                inputTextReader.getNextCharacter();
                return new Token(SPACE, currentTokenPosition);
            } else {
                return processContent();
            }
        }
    }

    private Token processContent() {
        String element = buildElement();
        return convertElementToToken(element, currentTokenPosition);
    }

    private Token convertElementToToken(String element, TokenPosition position){
        TokenType type = Mappings.CONST_HEADER_FIELDS_AND_VALUES.get(element);
        if(type != null){
            return new Token(type, position);
        } else {
            Token tokenToReturn = null;
            Optional<TokenType> optType = Mappings.PATTERN_FIELDS_AND_VALUES.stream()
                    .filter(it -> matchPattern(element, it.getKey()))
                    .map(Pair::getValue)
                    .findAny();
            if(optType.isPresent()){
                switch (optType.get()){
                    case BOUNDARY:
                    case CHARSET:
                    case FILE_NAME:
                    case NAME:
                        tokenToReturn = new Token(optType.get(), getStringBetweenQuotationMarks(element), position);
                        break;
                    case CONTENT_TYPE_VAL:
                    case ENCODING_VAL:
                    case DISPOSITION_VAL:
                        tokenToReturn = new Token(optType.get(), element, position);
                        break;
                    case SEPARATOR:
                        tokenToReturn = new Token(SEPARATOR, element.substring(2), position);
                        break;
                }
            } else {
                tokenToReturn = new Token(STRING, element, position);
            }
            return tokenToReturn;
        }
    }

    private String buildElement() {
        StringBuilder builder = new StringBuilder();
        builderLoop(builder);
        /*
        Colon should break building of element only in case it is header field element
        If it is string value colon should be included
         */
        if(inputTextReader.seeNextCharacter() == ':' && Mappings.CONST_HEADER_FIELDS_AND_VALUES.get(builder.toString()) == null){
            while (inputTextReader.seeNextCharacter() == ':'){
                builder.append(inputTextReader.getNextCharacter());
                builderLoop(builder);
            }
        }
        return builder.toString();
    }

    private void builderLoop(StringBuilder builder) {
        while(inputTextReader.hasNext() && isValidStringCharacter(inputTextReader.seeNextCharacter())){
            char c = inputTextReader.getNextCharacter();
            if(c == '"'){
                builder.append(c);
                buildInsideQuotationMarks(builder);
            } else {
                builder.append(c);
            }
        }
    }

    private void buildInsideQuotationMarks(StringBuilder builder) {
        while (inputTextReader.hasNext() && inputTextReader.seeNextCharacter() != '"'){
            builder.append(inputTextReader.getNextCharacter());
        }
        if(inputTextReader.hasNext()){
            builder.append(inputTextReader.getNextCharacter());
        }
    }

    TokenPosition getTokenPosition() {
        return new TokenPosition(
                inputTextReader.getPosition().getLine() + 1,
                inputTextReader.getPosition().getPositionInLine() + 1);
    }

    private void removeWhiteSpace(){
        while(inputTextReader.hasNext() && isWhiteChar(inputTextReader.seeNextCharacter())){
            inputTextReader.getNextCharacter();
        }
    }
}
