package com.ceyloncab.otpmgtservice.application.transport.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

    private String userId;
    @NotNull( message = "msisdn not found for operation. This action is not allowed" )
    @Valid
    private String msisdn;
    @NotNull( message = "firstName not found for operation. This action is not allowed" )
    @Valid
    private String firstName;
    @NotNull( message = "lastName not found for operation. This action is not allowed" )
    @Valid
    private String lastName;
    @NotNull( message = "location not found for operation. This action is not allowed" )
    @Valid
    private String location;
}