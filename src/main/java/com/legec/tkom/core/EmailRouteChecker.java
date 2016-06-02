package com.legec.tkom.core;

import com.legec.tkom.core.model.EmailHeader;
import com.legec.tkom.core.model.EmailType;
import com.legec.tkom.core.model.HeaderKey;

import java.util.List;
import java.util.stream.Collectors;

class EmailRouteChecker {
    private Filter filter;
    private EmailHeader header;

    EmailRouteChecker(Filter filter, EmailHeader header) {
        this.filter = filter;
        this.header = header;
    }

    void check(){
        checkComplianceFollowingServerAddresses();
        checkFromAndFirstAddressCompliance();
    }

    private void checkComplianceFollowingServerAddresses(){
        List<String> fromAddresses = header.getFieldValues(HeaderKey.RECEIVED).stream()
                .filter( val -> val.contains("from"))
                .map( val -> extractReceivedFromAddress(val))
                .collect(Collectors.toList());
        List<String> byAddresses = header.getFieldValues(HeaderKey.RECEIVED).stream()
                .filter( val -> val.contains("by"))
                .map( val -> extractReceivedByAddress(val))
                .collect(Collectors.toList());
        fromAddresses.remove(fromAddresses.size() - 1);
        matchAddressLists(fromAddresses, byAddresses);
    }

    private void matchAddressLists(List<String> fromList, List<String> byList){
        fromList.forEach( address -> {
            if(!byList.contains(address)){
                filter.addSuspiciousElement("Unexpected " + address + " server on email route");
                filter.setEmailType(EmailType.SPAM);
            }
        });
    }

    private void checkFromAndFirstAddressCompliance(){
        String from = header.getFieldValues(HeaderKey.FROM) != null ? header.getFieldValues(HeaderKey.FROM).get(0) : null;
        if(from == null || from.isEmpty()) {
            return;
        }
        String fromServer = from.contains("@") ? from.substring(from.indexOf('@')) : from;
        boolean matches = header.getFieldValues(HeaderKey.RECEIVED).stream()
                .filter( val -> val.contains("from"))
                .anyMatch(val -> Utils.stringIntersection(val, fromServer) >= fromServer.length() * 0.8);
        if(matches) {
            filter.addSuspiciousElement("Field 'from' inconsistent with the email route");
            filter.setEmailType(EmailType.SPAM);
        }
    }

    private static String extractReceivedByAddress(String line) {
        String mod1 = line.substring(line.indexOf("by")+ 3);
        return mod1.substring(0, mod1.indexOf(' '));
    }

    private static String extractReceivedFromAddress(String line) {
        String mod1 = line.substring(line.indexOf("from")+ 5);
        return mod1.substring(0, mod1.indexOf(' '));
    }
}