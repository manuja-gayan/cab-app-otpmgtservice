package com.ceyloncab.otpmgtservice.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class SendTextBody {
    @JsonProperty("transaction_id")
    private String transaction_id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("sourceAddress")
    private String sourceAddress;
    @JsonProperty("msisdn")
    private List<Msisdn> msisdn;

    public SendTextBody() {
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceAddress() {
        return this.sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public List<Msisdn> getMsisdn() {
        return this.msisdn;
    }

    public void setMsisdn(List<Msisdn> msisdn) {
        this.msisdn = msisdn;
    }
}
