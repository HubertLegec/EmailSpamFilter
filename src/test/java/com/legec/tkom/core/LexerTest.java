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
            add(new Token(TokenType.SPACE, new TokenPosition(1, 14)));
            add(new Token(TokenType.STRING, "hubert.legec@gmail.com", new TokenPosition(1, 15)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(1, 37)));
            add(new Token(TokenType.RECEIVED, new TokenPosition(2, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(2, 9)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 10)));
            add(new Token(TokenType.STRING, "by", new TokenPosition(2, 11)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 13)));
            add(new Token(TokenType.STRING, "10.37.47.132", new TokenPosition(2, 14)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 26)));
            add(new Token(TokenType.STRING, "with", new TokenPosition(2, 27)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 31)));
            add(new Token(TokenType.STRING, "SMTP", new TokenPosition(2, 32)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 36)));
            add(new Token(TokenType.STRING, "id", new TokenPosition(2, 37)));
            add(new Token(TokenType.SPACE, new TokenPosition(2, 39)));
            add(new Token(TokenType.STRING, "v126csp1212728ybv", new TokenPosition(2, 40)));
            add(new Token(TokenType.SEMICOLON, new TokenPosition(2, 57)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(2, 58)));
            add(new Token(TokenType.INDENTATION, new TokenPosition(3, 1)));
            add(new Token(TokenType.STRING, "Sun,", new TokenPosition(3, 9)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 13)));
            add(new Token(TokenType.NUMBER, "10", new TokenPosition(3, 14)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 16)));
            add(new Token(TokenType.STRING, "Apr", new TokenPosition(3, 17)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 20)));
            add(new Token(TokenType.NUMBER, "2016", new TokenPosition(3, 21)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 25)));
            add(new Token(TokenType.STRING, "23:46:50", new TokenPosition(3, 26)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 34)));
            add(new Token(TokenType.STRING, "-0700", new TokenPosition(3, 35)));
            add(new Token(TokenType.SPACE, new TokenPosition(3, 40)));
            add(new Token(TokenType.STRING, "(PDT)", new TokenPosition(3, 41)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(3, 46)));
            add(new Token(TokenType.TO, new TokenPosition(4, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(4, 3)));
            add(new Token(TokenType.SPACE, new TokenPosition(4, 4)));
            add(new Token(TokenType.STRING, "hubert.legec@gmail.com", new TokenPosition(4, 5)));
            add(new Token(TokenType.NEW_LINE, new TokenPosition(4, 27)));
            add(new Token(TokenType.CONTENT_TYPE, new TokenPosition(5, 1)));
            add(new Token(TokenType.COLON, new TokenPosition(5, 13)));
            add(new Token(TokenType.SPACE, new TokenPosition(5, 14)));
            add(new Token(TokenType.CONTENT_TEXT, "text/plain", new TokenPosition(5, 15)));
            add(new Token(TokenType.SEMICOLON, new TokenPosition(5, 25)));
            add(new Token(TokenType.SPACE, new TokenPosition(5, 26)));
            add(new Token(TokenType.CHARSET, "charset=\"UTF-8\"", new TokenPosition(5, 27)));
        }
    };

    private static final String[] SECOND_TEST_INPUT = {
            "From: zuraw@domena.pl",
            "To: czapla@domena.pl",
            "Subject: =?iso-8859-2?Q?Czaplo=2C_czy_um=F3wisz_si=EA_ze_mn=B1=3F?=",
            "MIME-Version: 1.0",
            "Content-Type: multipart/mixed; boundary=\"xxxToJestSeparator0000xxx\"",
            "",
            "--xxxToJestSeparator0000xxx",
            "Content-Type: multipart/alternative;",
            "      boundary=\"xxxToJestSeparatorZagniezdzony1111xxx\"",
            "",
            "--xxxToJestSeparatorZagniezdzony1111xxx",
            "Content-Type: text/plain; charset=\"iso-8859-2\"",
            "Content-Transfer-Encoding: quoted-printable",
            "",
            "To jest tre=B6=E6 wiadomo=B6ci.",
            "--xxxToJestSeparatorZagniezdzony1111xxx",
            "Content-Type: text/html; charset=\"iso-8859-2\"",
            "Content-Transfer-Encoding: quoted-printable",
            "",
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">",
            "<HTML><HEAD>",
            "<META http-equiv=3DContent-Type content=3D\"text/html; charset=3Diso-8859-2\"></HEAD>",
            "<BODY><FONT face=3DArial size=3D2>To jest tre=B6=E6 wiadomo=B6ci.</FONT></BODY></HTML>",
            "",
            "--xxxToJestSeparatorZagniezdzony1111xxx--",
            "",
            "--xxxToJestSeparator0000xxx",
            "Content-Type: image/gif; name=\"obrazek.gif\"",
            "Content-Transfer-Encoding: base64",
            "Content-Disposition: attachment; filename=\"obrazek.gif\"",
            "",
            "PGh0bWw+CiAgPGhlYWQ+CiAgPC9oZWFkPgogIDxib2R5PgogICAgPHA+VGhpcyBpcyB0aGUg",
            "Ym9keSBvZiB0aGUgbWVzc2FnZS48L3A+CiAgPC9ib2R5Pgo8L2h0bWw+Cg==",
            "",
            "--xxxToJestSeparator0000xxx--"
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
            if(testToken.getTokenType() == TokenType.STRING){
                assertEquals("tokens have the same values", testToken.getValue(), lexerToken.getValue());
            }
        });
        assertNull("next token is empty", lexer.getNextToken());
    }

    @Test
    public void completeEmailTest(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(SECOND_TEST_INPUT);
        Lexer lexer = new Lexer(emailReader);

        Token token = lexer.getNextToken();
        while(token != null){
            System.out.println(token.getTokenType().name());
            token = lexer.getNextToken();
        }
    }
}
