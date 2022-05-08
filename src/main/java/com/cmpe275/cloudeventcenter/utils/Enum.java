package com.cmpe275.cloudeventcenter.utils;

public class Enum {

    public enum AdmissionPolicy {
        FirstComeFirstServe,
        OnApproval
    }

    public enum EventStatus {
        SignUpOpen,
        SignUpClosed,
        Ongoing,
        Finished,
        Cancelled,
    }

    public enum AccountType {
        Person,
        Organization
    }
}
