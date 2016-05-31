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
    X_RECEIVED("X-Received"),
    RETURN_PATH("Return-Path"),
    MIME_VERSION("MIME-Version"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_TRANSFER_ENCODING("Content-Transfer-Encoding"),
    CONTENT_DISPOSITION("Content-Disposition"),
    MESSAGE_ID("Message-ID"),
    REPLY_TO("Reply-To"),
    SENDER("Sender"),
    LIST_UNSUBSCRIBE("List-Unsubscribe"),
    //------ headers values ------
    ENCODING_VAL("(base64)|(quoted-printable)|(7bit)"),
    CONTENT_TYPE_VAL("((multipart)|(application)|(text)|(image)|(audio))/[a-zA-Z]\\S+[a-zA-Z]"),
    DISPOSITION_VAL("(attachment)|(inline)"),
    BOUNDARY("boundary=\"\\S+\""),
    CHARSET("charset=\"[0-9a-zA-Z-]+\""),
    FILE_NAME("filename=\"\\S+\""),
    NAME("name=\"\\S+\""),
    //------ others ------
    INDENTATION("\\t|' '{2,}(\\t|' ')*"),
    SPACE(" "),
    NEW_LINE("\\n"),
    COLON(":"),
    SEMICOLON(";"),
    SEPARATOR("--\\S+"),
    STRING("[^;:\\n\\t\\s]|([^;:\\n\\t\\s]\\S*[^;:\\n\\t\\s])|(\"(.|\\n)*\")");


    private String pattern;

    TokenType(String pattern){
        this.pattern = pattern;
    }

    public String getPattern(){
        return pattern;
    }
}
