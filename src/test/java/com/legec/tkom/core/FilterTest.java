package com.legec.tkom.core;

import com.legec.tkom.core.configuration.Configuration;
import com.legec.tkom.core.configuration.GlobalConfig;
import com.legec.tkom.core.model.EmailType;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FilterTest {

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

    @Test
    public void firstMultipartTest(){
        Configuration configuration = new Configuration();
        configuration.setDangerousExtensions(Arrays.asList("gif"));
        GlobalConfig.setConfiguration(configuration);
        EmailReader emailReader = new EmailReader();
        emailReader.setLines(MULTIPART_MESSAGE_INPUT);
        Lexer lexer = new Lexer(emailReader);
        Parser parser = new Parser(lexer);
        parser.parse();
        Filter filter = new Filter(parser.getModel());
        EmailType type = filter.processEmail();

        assertEquals(EmailType.SPAM, type);
    }
}
