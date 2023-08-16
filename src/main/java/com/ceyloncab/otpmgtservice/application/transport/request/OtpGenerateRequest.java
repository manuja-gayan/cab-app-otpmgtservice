package com.ceyloncab.otpmgtservice.application.transport.request;

import com.ceyloncab.otpmgtservice.domain.utils.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpGenerateRequest {

    @NotNull( message = "msisdn not found for operation. This action is not allowed" )
    @Valid
    private String msisdn;
    @NotNull( message = "action not found for operation. This action is not allowed" )
    @Valid
    private Action action;
}