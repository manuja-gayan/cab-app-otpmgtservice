package com.ceyloncab.otpmgtservice.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class TokenResponse {
    @JsonProperty("errCode")
    private String errCode;
    @JsonProperty("userData")
    private UserData userData;
    @JsonProperty("refreshExpiration")
    private int refreshExpiration;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("expiration")
    private int expiration;
    @JsonProperty("remainingCount")
    private int remainingCount;
    @JsonProperty("token")
    private String token;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("status")
    private String status;

    public TokenResponse() {
    }

    public String getErrCode() {
        return this.errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public int getRefreshExpiration() {
        return this.refreshExpiration;
    }

    public void setRefreshExpiration(int refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public int getExpiration() {
        return this.expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public int getRemainingCount() {
        return this.remainingCount;
    }

    public void setRemainingCount(int remainingCount) {
        this.remainingCount = remainingCount;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
