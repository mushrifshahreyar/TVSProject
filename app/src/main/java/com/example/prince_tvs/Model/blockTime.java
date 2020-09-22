package com.example.prince_tvs.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class blockTime {
    private int paidService;
    private int freeService;
    private int runningService;
    private String date;

    public void setFreeService(int freeService) {
        this.freeService = freeService;
    }

    public void setPaidService(int paidService) {
        this.paidService = paidService;
    }

    public void setRunningService(int runningService) {
        this.runningService = runningService;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFreeService() {
        return freeService;
    }

    public int getPaidService() {
        return paidService;
    }

    public int getRunningService() {
        return runningService;
    }

    public String getDate() {
        return date;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("paidService", paidService);
        result.put("freeService", freeService);
        result.put("runningService", runningService);

        return result;
    }
}
