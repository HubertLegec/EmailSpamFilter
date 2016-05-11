package com.legec.tkom.core;

import com.legec.tkom.core.model.Token;
import com.legec.tkom.core.model.TokenPosition;
import com.legec.tkom.core.model.TokenType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class LexerTest {
    private static final String[] FIRST_TEST_INPUT = {
            "Delivered-To: hubert.legec@gmail.com",
            "Received: by 10.37.47.132 with SMTP id v126csp1212728ybv;",
            "        Sun, 10 Apr 2016 23:46:50 -0700 (PDT)",
            "To: hubert.legec@gmail.com",
            "Content-Type: text/plain; charset=\"UTF-8\""
    };

    private static final List<Token> FIRST_TEST_TOKENS = new ArrayList<Token>(){
        {
            add(new Token(TokenType.DELIVERED_TO, new TokenPosition(1, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(1, 13)));
            add(new Token(TokenType.STRING_VALUE, "hubert.legec@gmail.com", new TokenPosition(1, 15)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(1, 37)));
            add(new Token(TokenType.RECEIVED, new TokenPosition(2, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(2, 9)));
            add(new Token(TokenType.STRING_VALUE, "by 10.37.47.132 with SMTP id v126csp1212728ybv", new TokenPosition(2, 11)));
            add(new Token(TokenType.SEMICOLON, new TokenPosition(2, 57)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(2, 58)));
            add(new Token(TokenType.INDENTATION, new TokenPosition(3, 1)));
            add(new Token(TokenType.STRING_VALUE, "Sun, 10 Apr 2016 23:46:50 -0700 (PDT)", new TokenPosition(3, 9)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(3, 46)));
            add(new Token(TokenType.TO, new TokenPosition(4, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(4, 3)));
            add(new Token(TokenType.STRING_VALUE, "hubert.legec@gmail.com", new TokenPosition(4, 5)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(4, 27)));
            add(new Token(TokenType.CONTENT_TYPE, new TokenPosition(5, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(5, 13)));
            add(new Token(TokenType.STRING_VALUE, "text/plain", new TokenPosition(5, 15)));
            add(new Token(TokenType.SEMICOLON, new TokenPosition(5, 25)));
            add(new Token(TokenType.STRING_VALUE, "charset=\"UTF-8\"", new TokenPosition(5, 27)));
        }
    };

    @Test
    public void whenInputIsEmptyNullTokenShouldBeReturned(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(new String[0]);
        Lexer lexer = new Lexer(emailReader);

        assertNull("next token is null", lexer.getNextToken());
    }

    @Test
    public void whenInputIsValidProperTokensShouldBeReturned(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(FIRST_TEST_INPUT);
        Lexer lexer = new Lexer(emailReader);

        FIRST_TEST_TOKENS.forEach( testToken -> {
            Token lexerToken = lexer.getNextToken();
            assertNotNull("lexer token is not null", lexerToken);
            assertEquals("tokens have the same type", testToken.getTokenType(), lexerToken.getTokenType());
            assertEquals("tokens have the same position", testToken.getPosition(), lexerToken.getPosition());
            if(testToken.getTokenType() == TokenType.STRING_VALUE){
                assertEquals("tokens have the same values", testToken.getValue(), lexerToken.getValue());
            }
        });
        assertNull("next token is empty", lexer.getNextToken());
    }
}
