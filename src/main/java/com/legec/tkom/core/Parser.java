package com.legec.tkom.core;

import com.legec.tkom.core.model.*;

import java.util.ArrayList;
import java.util.List;

import static com.legec.tkom.core.Utils.isHeaderKeyIn;
import static com.legec.tkom.core.Utils.isTokenTypeIn;
import static com.legec.tkom.core.model.ExceptionMessage.*;
import static com.legec.tkom.core.model.TokenType.*;
import static java.util.Arrays.asList;

class Parser {
    private final Lexer lexer;
    private EmailModel model = new EmailModel();
    private Token token = null;
    private BodyPart currentBodyPart = null;

    Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    void parse() throws ParserException {
        reset();
        nextToken();
        parseHeader();
        parseBody();
    }

    private void parseHeader() throws ParserException {
        checkIfPartExist(HEADER_DOES_NOT_EXIST);
        do {
            parseHeaderRow();
        } while (token != null && token.isHeaderRowKey());
    }

    private void parseBody() throws ParserException {
        checkIfPartExist(BODY_DOES_NOT_EXIST);
        if (!model.isMultipart()) {
            currentBodyPart = new BodyPart();
            StringBuilder bodyBuilder = new StringBuilder();
            skipNewLines();
            while (token != null) {
                bodyBuilder.append(token.getWrappedValue());
                nextToken();
            }
            currentBodyPart.setBody(bodyBuilder.toString());
            addBodyPartToModel();
        } else {
            boolean shouldContinue = true;
            while (token != null && shouldContinue) {
                skipNewLines();
                checkIfPartExist(BODY_PART_DOES_NOT_EXIST);
                currentBodyPart = new BodyPart();
                shouldContinue = parseBodyPart();
                addBodyPartToModel();
            }
            checkIfBodyEnd();
        }
    }

    private void checkIfBodyEnd() {
        do {
            nextToken();
        } while (isTokenTypeIn(token, asList(SPACE, TABULATION, NEW_LINE)));
        if (token != null) {
            throw new ParserException(END_OF_EMAIL_EXPECTED, lexer.getTokenPosition());
        }
    }

    private boolean parseBodyPart() throws ParserException {
        if (token.getTokenType() != SEPARATOR) {
            throw new ParserException(BODY_PART_SEPARATOR_DOES_NOT_EXIST, token.getPosition());
        } else {
            do {
                nextToken();
            } while (token.getTokenType() == NEW_LINE);
        }
        parseHeader();
        skipNewLines();
        StringBuilder bodyBuilder = new StringBuilder();
        while (token != null && token.getTokenType() != SEPARATOR) {
            bodyBuilder.append(token.getWrappedValue());
            nextToken();
        }
        currentBodyPart.setBody(bodyBuilder.toString());
        if (token.getTokenType() == SEPARATOR) {
            return !isEndSeparator();
        } else {
            TokenPosition pos = token != null ? token.getPosition() : lexer.getTokenPosition();
            throw new ParserException(END_SEPARATOR_DOES_NOT_EXIST, pos);
        }
    }

    private void skipNewLines() {
        while (token != null && token.getTokenType() == NEW_LINE) {
            nextToken();
        }
    }

    private void parseHeaderRow() {
        HeaderKey key = readHeaderRowKey();
        goToValue();
        List<String> values = new ArrayList<>();
        while(true) {
            readHeaderRowValue(key, values);
            if (isTokenTypeIn(token, asList(SPACE, TABULATION, INDENTATION))) {
                nextToken();
            } else {
                break;
            }
        }
        addHeaderPart(key, values);
    }

    private HeaderKey readHeaderRowKey() {
        if (!token.isHeaderRowKey()) {
            throw new ParserException(HEADER_KEY_EXPECTED, token.getPosition());
        }
        return HeaderKey.getFromTokenType(token.getTokenType());
    }

    private void readHeaderRowValue(HeaderKey key, List<String> values) {
        StringBuilder builder = new StringBuilder();
        while (!isTokenTypeIn(token, asList(NEW_LINE, SEMICOLON))) {
            if (isTokenTypeIn(token, asList(SPACE, TABULATION, STRING))) {
                builder.append(token.getWrappedValue());
            } else if (isHeaderKeyIn(key, asList(HeaderKey.CONTENT_TYPE, HeaderKey.CONTENT_TRANSFER_ENCODING, HeaderKey.CONTENT_DISPOSITION))) {
                addKeyValues(builder, key);
            }  else {
                throw new ParserException(UNEXPECTED_HEADER_ROW_VALUE, token.getPosition());
            }
            nextToken();
        }
        if (token.getTokenType() == SEMICOLON) {
            nextToken();
        }
        if (token.getTokenType() == NEW_LINE) {
            nextToken();
        }
        values.add(builder.toString());
    }

    private void addKeyValues(StringBuilder builder, HeaderKey key) {
        if (Mappings.HEADER_KEY_VALUES.get(key).contains(token.getTokenType())) {
            builder.append(token.getWrappedValue());
        } else {
            throw new ParserException(UNEXPECTED_HEADER_ROW_VALUE, token.getPosition());
        }
    }

    private void checkIfPartExist(ExceptionMessage message) throws ParserException {
        if (token == null) {
            throw new ParserException(message, lexer.getTokenPosition());
        }
    }

    private void goToValue() {
        nextToken();
        while (isTokenTypeIn(token, asList(COLON, SPACE, INDENTATION, TABULATION))) {
            nextToken();
        }
    }

    private boolean isEndSeparator() {
        return token != null &&
                token.getTokenType() == SEPARATOR &&
                token.getValue().endsWith("--");
    }

    private void addBodyPartToModel() {
        if (currentBodyPart != null) {
            model.getBodyParts().add(currentBodyPart);
            currentBodyPart = null;
        }
    }

    private void addHeaderPart(HeaderKey key, List<String> values) {
        if (currentBodyPart == null) {
            model.addMainHeaderRow(key, values);
        } else {
            currentBodyPart.addHeaderRow(key, values);
        }
    }

    private void nextToken() {
        token = lexer.getNextToken();
    }

    EmailModel getModel() {
        return model;
    }

    private void reset() {
        model = new EmailModel();
        token = null;
        currentBodyPart = null;
    }
}
