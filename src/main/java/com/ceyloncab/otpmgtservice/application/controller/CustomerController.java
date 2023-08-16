/**
 * Copyrights 2020 Axiata Digital Labs Pvt Ltd.
 * All Rights Reserved.
 * <p>
 * These material are unpublished, proprietary, confidential source
 * code of Axiata Digital Labs Pvt Ltd (ADL) and constitute a TRADE
 * SECRET of ADL.
 * <p>
 * ADL retains all title to and intellectual property rights in these
 * materials.
 */
package com.ceyloncab.otpmgtservice.application.controller;

import com.ceyloncab.otpmgtservice.application.transport.request.CustomerRequest;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.otpmgtservice.domain.service.CustomerManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *  * This is the controller class for Customer management.
 */
@RestController
@RequestMapping("${base-url.context}/customer")
public class CustomerController extends BaseController {

    @Autowired
    CustomerManagementService customerManagementService;

    @PostMapping(value = "/profile/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createCustomerProfile(@Validated @RequestBody(required = true) CustomerRequest createRequest, HttpServletRequest request) {

        CommonResponse response = customerManagementService.createCustomerProfile(createRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }

    @PostMapping(value ="/profile/update", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<Object> updateCustomerProfile(@Validated @RequestBody(required = true) CustomerRequest customerRequest,HttpServletRequest request){
        CommonResponse response =customerManagementService.updateCustomerProfile(customerRequest);
        return getResponseEntity(response.getResponseHeader().getResponseCode(),response);
    }

    @GetMapping(value = "/profile/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCustomerProfile(HttpServletRequest request){
        CommonResponse response = customerManagementService.getCustomerProfile(request.getHeader("UserId"));
        return getResponseEntity(response.getResponseHeader().getResponseCode(), response);
    }
}
