package com.legec.tkom.core.model;

public enum TokenType {
    //------ headers ------
    DATE("Date"),
    FROM("From"),
    TO("To"),
    CC("Cc"),
    BCC("Bcc"),
    SUBJECT("Subject"),
    DELIVERED_TO("Delivered-To"),
    RECEIVED("Received"),
    RETURN_PATH("Return-Path"),
    MIME_VERSION("MIME-Version"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding"),
    CONTENT_DISPOSITION("Content-Disposition"),
    MESSAGE_ID("Message-ID"),
    REPLY_TO("Reply-To"),
    SENDER("Sender"),
    //------ headers values ------
    ENCODING_BASE64("base64"),
    ENCODING_QUOTED_PRINTABLE("quoted-printable"),
    CONTENT_MULTIPART("multipart/[a-z]+"),
    CONTENT_TEXT("text/[a-z]+"),
    CONTENT_IMAGE("image/[a-z.]+"),
    CONTENT_APPLICATION("application/[a-z+-]"),
    BOUNDARY("boundary=\"\\S+\""),
    CHARSET("charset=\"[0-9a-zA-Z-]+\""),
    FILE_NAME("filename=\"\\S+\""),
    //------ others ------
    INDENTATION("\\t|' '{2,}(\\t|' ')*"),
    SPACE(" "),
    NEW_LINE("\\n"),
    COLON(":"),
    SEMICOLON(";"),
    SEPARATOR("--\\S+"),
    NUMBER("\\d+|(\\d+.\\d*)"),
    STRING("[^;:\\n\\t\\s]|([^;:\\n\\t\\s]\\S*[^;:\\n\\t\\s])|(\"(.|\\n)*\")");


    private String pattern;

    TokenType(String pattern){
        this.pattern = pattern;
    }

    public String getPattern(){
        return pattern;
    }
}
