package com.ceyloncab.otpmgtservice.application.controller;


import com.ceyloncab.otpmgtservice.application.transport.request.OtpGenerateRequest;
import com.ceyloncab.otpmgtservice.application.transport.request.OtpValidateRequest;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.otpmgtservice.domain.service.OTPService;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * This is the controller class for otp generation and validation .
 *
 */
@Slf4j
@RestController
@RequestMapping("${base-url.context}")
public class OTPServiceController extends BaseController {
    @Autowired
    OTPService otpService;

    @Value("${appMode}")
    String appMode;

    @Value("${white-list.numbers}")
    List<String> whitelistNumArray;

    /**
     * perform otp generation
     *
     * @param otpRequest: otp generation request body
     * @param request:    request header
     * @return otp generation response
     * @throws Exception
     */
    @PostMapping(value = "/otp-generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateOTP(@Validated @RequestBody(required = true) OtpGenerateRequest otpRequest, HttpServletRequest request) {

        CommonResponse otpGenerationResponse = otpService.otpGenerationService(otpRequest);
        return getResponseEntity(otpGenerationResponse.getResponseHeader().getResponseCode(), otpGenerationResponse);
    }

    /**
     * perform otp validation
     *
     * @param otpRequest: otp validation request body
     * @param request:    validation request header
     * @return otp validation response
     */
    @PostMapping(value = "/otp-validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateOTP(@RequestBody(required = true) OtpValidateRequest otpRequest, HttpServletRequest request) {

        if (whitelistNumArray.contains(otpRequest.getMsisdn()) && ("1111".equals(otpRequest.getOtp()))) {
                        CommonResponse res = new CommonResponse();
            ResponseHeader responseHeader = new ResponseHeader(Constants.ResponseData.VERIFY_SUCCESS);
            res.setResponseHeader(responseHeader);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        }

        CommonResponse otpValidationResponse = otpService.otpValidationService(otpRequest);

        return getResponseEntity(otpValidationResponse.getResponseHeader().getResponseCode(), otpValidationResponse);
    }

}
