package com.legec.tkom.core;

import org.junit.Test;

public class ParserTest {

    private static final String[] FIRST_TEST_INPUT = {
            "Delivered-To: hubert.legec@gmail.com",
            "Received: by 10.37.47.132 with SMTP id v126csp1212728ybv;",
            "        Sun, 10 Apr 2016 23:46:50 -0700 (PDT)",
            "To: hubert.legec@gmail.com",
            "Content-Type: text/plain; charset=\"UTF-8\"",
            ""
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
    public void firstTest(){
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(FIRST_TEST_INPUT);
        Lexer lexer = new Lexer(emailReader);
        Parser parser = new Parser(lexer);
        parser.parse();
    }
}
