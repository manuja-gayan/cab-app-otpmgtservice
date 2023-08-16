package com.ceyloncab.otpmgtservice.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class UserData {
    @JsonProperty("walletBalance")
    private double walletBalance;
    @JsonProperty("additional_mask")
    private List<Object> additional_mask;
    @JsonProperty("defaultMask")
    private String defaultMask;
    @JsonProperty("email")
    private String email;
    @JsonProperty("mobile")
    private int mobile;
    @JsonProperty("address")
    private String address;
    @JsonProperty("lname")
    private String lname;
    @JsonProperty("fname")
    private String fname;
    @JsonProperty("id")
    private int id;

    public UserData() {
    }

    public double getWalletBalance() {
        return this.walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public List<Object> getAdditional_mask() {
        return this.additional_mask;
    }

    public void setAdditional_mask(List<Object> additional_mask) {
        this.additional_mask = additional_mask;
    }

    public String getDefaultMask() {
        return this.defaultMask;
    }

    public void setDefaultMask(String defaultMask) {
        this.defaultMask = defaultMask;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMobile() {
        return this.mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLname() {
        return this.lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return this.fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
