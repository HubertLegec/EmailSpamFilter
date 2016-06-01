package com.legec.tkom.core;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void QuotedPrintableTextDecodingTest(){
        String text = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"><HTML><HEAD><META http-equiv=3DContent-Type content=3D\"text/html; charset=3Diso-8859-2\"></HEAD><BODY><FONT face=3DArial size=3D2>To jest tre=B6=E6 wiadomo=B6ci.</FONT></BODY></HTML>";

        String result = Utils.decodeString(text, "quoted-printable", "iso-8859-2");

        System.out.println(result);
    }
}
