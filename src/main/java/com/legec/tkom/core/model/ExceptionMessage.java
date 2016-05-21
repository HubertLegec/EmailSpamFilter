package com.legec.tkom.core.model;

public enum ExceptionMessage {
    HEADER_DOES_NOT_EXIST("Header expected"),
    BODY_DOES_NOT_EXIST("Body expected"),
    HEADER_KEY_EXPECTED("Header key expected"),
    UNEXPECTED_HEADER_ROW_VALUE("Unexpected header row value");


    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
