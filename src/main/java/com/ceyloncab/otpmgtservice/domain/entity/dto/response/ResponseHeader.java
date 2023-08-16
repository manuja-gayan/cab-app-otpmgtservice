package com.ceyloncab.otpmgtservice.domain.entity.dto.response;

import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseHeader {
    private String code;
    private String message;
    @JsonIgnore
    private String responseCode;

    public ResponseHeader(Constants.ResponseData response){
        this.code=response.getCode();
        this.message=response.getMessage();
        this.responseCode=response.getResponseCode();
    }
}