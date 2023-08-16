package com.ceyloncab.otpmgtservice.domain.exception;

import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DomainException extends RuntimeException{
    private String code;
    private String message;
    @JsonIgnore
    private String responseCode = "400";

    public DomainException(Constants.ResponseData response){
        this.code=response.getCode();
        this.message=response.getMessage();
        this.responseCode=response.getResponseCode();
    }
}
