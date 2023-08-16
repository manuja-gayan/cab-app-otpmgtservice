package com.ceyloncab.otpmgtservice.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OtpValidateRequest extends OtpGenerateRequest{

    @NotNull( message = "otp not found for operation. This action is not allowed" )
    @Valid
    private String otp;
}