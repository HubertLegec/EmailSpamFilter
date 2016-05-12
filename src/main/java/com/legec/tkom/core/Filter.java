package com.legec.tkom.core;

import com.legec.tkom.core.model.EmailModel;
import com.legec.tkom.core.model.EmailType;

import java.util.ArrayList;
import java.util.List;

class Filter {
    private EmailModel model;
    private List<String> suspiciousElements = new ArrayList<>();

    Filter(EmailModel model) {
        this.model = model;
    }

    EmailType processEmail(){
        //TODO
        return EmailType.OK;
    }

    List<String> getSuspiciousElements(){
        return suspiciousElements;
    }
}
