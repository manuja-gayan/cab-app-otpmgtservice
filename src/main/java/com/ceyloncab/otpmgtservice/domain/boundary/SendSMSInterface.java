package com.ceyloncab.otpmgtservice.domain.boundary;

/**
 * This is domain interface to call external service to send SMS.
 */
public interface SendSMSInterface {
    public void sendOTP(String msisdn, String otp);
}
