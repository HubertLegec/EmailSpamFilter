package com.legec.tkom.core.model;

import java.util.ArrayList;
import java.util.List;

public class EmailModel {
    private EmailHeader emailHeader = new EmailHeader();
    private List<BodyPart> bodyParts = new ArrayList<>();

    public EmailHeader getEmailHeader() {
        return emailHeader;
    }

    public List<BodyPart> getBodyParts() {
        return bodyParts;
    }
}
