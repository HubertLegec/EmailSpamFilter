package com.legec.tkom.core.configuration;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    private List<String> dangerousExtensions = new ArrayList<>();
    private List<String> suspiciousWords = new ArrayList<>();
    private List<String> suspiciousTitleWords = new ArrayList<>();
    private List<String> dangerousServers = new ArrayList<>();
    private boolean checkRoute;

    public Configuration() {}

    public Configuration(List<String> dangerousExtensions, List<String> suspiciousWords, List<String> suspiciousTitleWords, List<String> dangerousServers, boolean checkRoute) {
        this.dangerousExtensions = dangerousExtensions;
        this.suspiciousWords = suspiciousWords;
        this.suspiciousTitleWords = suspiciousTitleWords;
        this.dangerousServers = dangerousServers;
        this.checkRoute = checkRoute;
    }

    public List<String> getDangerousExtensions() {
        return dangerousExtensions;
    }

    public void setDangerousExtensions(List<String> dangerousExtensions) {
        this.dangerousExtensions = dangerousExtensions;
    }

    public List<String> getSuspiciousWords() {
        return suspiciousWords;
    }

    public void setSuspiciousWords(List<String> suspiciousWords) {
        this.suspiciousWords = suspiciousWords;
    }

    public List<String> getSuspiciousTitleWords() {
        return suspiciousTitleWords;
    }

    public void setSuspiciousTitleWords(List<String> suspiciousTitleWords) {
        this.suspiciousTitleWords = suspiciousTitleWords;
    }

    public List<String> getDangerousServers() {
        return dangerousServers;
    }

    public void setDangerousServers(List<String> dangerousServers) {
        this.dangerousServers = dangerousServers;
    }

    public boolean isCheckRoute() {
        return checkRoute;
    }

    public void setCheckRoute(boolean checkRoute) {
        this.checkRoute = checkRoute;
    }
}
