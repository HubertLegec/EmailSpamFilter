package com.legec.tkom.core;

import com.legec.tkom.core.model.ParserException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    private static final String[] FIRST_TEST_INPUT = {
            "Delivered-To: hubert.legec@gmail.com",
            "Received: by 10.37.47.132 with SMTP id v126csp1212728ybv;",
            "        Sun, 10 Apr 2016 23:46:50 -0700 (PDT)",
            "To: hubert.legec@gmail.com",
            "Content-Type: text/plain; charset=\"UTF-8\"",
            ""
    };

    private static final String[] PLAIN_TEXT_MESSAGE_INPUT = {
            "From: zuraw@domena.pl",
            "To: czapla@domena.pl",
            "Subject: =?iso-8859-2?Q?Czaplo=2C_czy_um=F3wisz_si=EA_ze_mn=B1=3F?=",
            "MIME-Version: 1.0",
            "Content-Type: text/plain",
            "List-Unsubscribe: <mailto:unsubscribe-espc-tech-12345N@domain.com>, <http://domain.com/member/unsubscribe/?listname=espc-tech@domain.com?id=12345N>",
            "",
            "Hi Tom,",
            "",
            "How are you?",
            "Call me ASAP!",
            "",
            "Waiting to hear you,",
            "Anna"
    };

    private static final String[] MULTIPART_MESSAGE_INPUT = {
            "From: zuraw@domena.pl",
            "To: czapla@domena.pl",
            "Subject: =?iso-8859-2?Q?Czaplo=2C_czy_um=F3wisz_si=EA_ze_mn=B1=3F?=",
            "MIME-Version: 1.0",
            "Content-Type: multipart/mixed; boundary=\"xxxToJestSeparator0000xxx\"",
            "",
            "--xxxToJestSeparator0000xxx",
            "Content-Type: text/html; charset=\"iso-8859-2\"",
            "Content-Transfer-Encoding: quoted-printable",
            "",
            "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">",
            "<HTML><HEAD>",
            "<META http-equiv=3DContent-Type content=3D\"text/html; charset=3Diso-8859-2\"></HEAD>",
            "<BODY><FONT face=3DArial size=3D2>To jest tre=B6=E6 wiadomo=B6ci.</FONT></BODY></HTML>",
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

    @Test(expected = ParserException.class)
    public void firstTest(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(FIRST_TEST_INPUT);
        Lexer lexer = new Lexer(emailReader);
        Parser parser = new Parser(lexer);
        parser.parse();
    }

    @Test
    public void plainTextMessageTest(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(PLAIN_TEXT_MESSAGE_INPUT);
        Lexer lexer = new Lexer(emailReader);
        Parser parser = new Parser(lexer);
        parser.parse();

        assertFalse(parser.getModel().isMultipart());
        assertEquals(parser.getModel().getEmailHeader().getHeaderParts().size(), 6);
        assertEquals(parser.getModel().getBodyParts().size(), 1);
        assertFalse(parser.getModel().getBodyParts().get(0).getBody().isEmpty());
    }

    @Test
    public void multipartMessageTest(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(MULTIPART_MESSAGE_INPUT);
        Lexer lexer = new Lexer(emailReader);
        Parser parser = new Parser(lexer);
        parser.parse();

        assertTrue(parser.getModel().isMultipart());
    }
}
