package com.legec.tkom.core.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class PatternTest {

    @Test
    public void encodingValueTest(){
        Pattern pattern = Pattern.compile(TokenType.ENCODING_VAL.getPattern());
        List<String> acceptableValues = new ArrayList<>();
        acceptableValues.add("base64");
        acceptableValues.add("quoted-printable");
        acceptableValues.add("7bit");

        acceptableValues.forEach( val ->
                assertTrue("encoding type matches pattern", pattern.matcher(val).matches())
        );
    }

    @Test
    public void contentTypeValueTest(){
        Pattern pattern = Pattern.compile(TokenType.CONTENT_TYPE_VAL.getPattern());
        List<String> acceptableValues = new ArrayList<>();
        acceptableValues.add("application/javascript");
        acceptableValues.add("application/octet-stream");
        acceptableValues.add("audio/mpeg");
        acceptableValues.add("audio/vnd.rn-realaudio");
        acceptableValues.add("image/jpeg");
        acceptableValues.add("image/vnd.microsoft.icon");
        acceptableValues.add("multipart/mixed");
        acceptableValues.add("text/html");

        acceptableValues.forEach( val ->
                assertTrue("content type matches pattern", pattern.matcher(val).matches())
        );
    }

    @Test
    public void boundaryTest(){
        Pattern pattern = Pattern.compile(TokenType.BOUNDARY.getPattern());
        List<String> acceptableValues = new ArrayList<>();
        acceptableValues.add("boundary=\"askjgdosbir43642egjpi32ga\"");
        acceptableValues.add("boundary=\"=34vdfg9*^%hdfidgfsvc34\"");

        acceptableValues.forEach( val ->
                assertTrue("boundary matches pattern", pattern.matcher(val).matches())
        );
    }
}
