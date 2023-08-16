package com.ceyloncab.otpmgtservice.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class Data {
    @JsonProperty("invalidNumbers")
    private int invalidNumbers;
    @JsonProperty("duplicatesRemoved")
    private int duplicatesRemoved;
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("userMobile")
    private int userMobile;
    @JsonProperty("walletBalance")
    private double walletBalance;
    @JsonProperty("campaignCost")
    private double campaignCost;
    @JsonProperty("campaignId")
    private int campaignId;

    public Data() {
    }

    public int getInvalidNumbers() {
        return this.invalidNumbers;
    }

    public void setInvalidNumbers(int invalidNumbers) {
        this.invalidNumbers = invalidNumbers;
    }

    public int getDuplicatesRemoved() {
        return this.duplicatesRemoved;
    }

    public void setDuplicatesRemoved(int duplicatesRemoved) {
        this.duplicatesRemoved = duplicatesRemoved;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserMobile() {
        return this.userMobile;
    }

    public void setUserMobile(int userMobile) {
        this.userMobile = userMobile;
    }

    public double getWalletBalance() {
        return this.walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    public double getCampaignCost() {
        return this.campaignCost;
    }

    public void setCampaignCost(double campaignCost) {
        this.campaignCost = campaignCost;
    }

    public int getCampaignId() {
        return this.campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
}
