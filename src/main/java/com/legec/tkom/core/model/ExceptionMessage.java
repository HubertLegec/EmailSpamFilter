package com.legec.tkom.core.model;

public enum ExceptionMessage {
    HEADER_DOES_NOT_EXIST("Header expected"),
    BODY_DOES_NOT_EXIST("Body expected"),
    BODY_PART_DOES_NOT_EXIST("Body part expected"),
    BODY_PART_SEPARATOR_DOES_NOT_EXIST("Body part separator expected"),
    END_SEPARATOR_DOES_NOT_EXIST("End separator expected"),
    HEADER_KEY_EXPECTED("Header key expected"),
    END_OF_EMAIL_EXPECTED("End of email expected"),
    UNEXPECTED_HEADER_ROW_VALUE("Unexpected header row value");

    private String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
