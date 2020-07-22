package com.example.clik.Model;

public class Call {
    String callBy;
    String callTo;

    public Call(String callBy, String callTo) {
        this.callBy = callBy;
        this.callTo = callTo;
    }

    public Call() {
    }

    public String getCallBy() {
        return callBy;
    }

    public void setCallBy(String callBy) {
        this.callBy = callBy;
    }

    public String getCallTo() {
        return callTo;
    }

    public void setCallTo(String callTo) {
        this.callTo = callTo;
    }
}
