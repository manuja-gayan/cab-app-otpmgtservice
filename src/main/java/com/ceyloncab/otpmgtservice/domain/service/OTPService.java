package com.ceyloncab.otpmgtservice.domain.service;

import com.ceyloncab.otpmgtservice.application.aop.AopConstants;
import com.ceyloncab.otpmgtservice.application.transport.request.OtpGenerateRequest;
import com.ceyloncab.otpmgtservice.application.transport.request.OtpValidateRequest;
import com.ceyloncab.otpmgtservice.domain.boundary.SendSMSInterface;
import com.ceyloncab.otpmgtservice.domain.entity.OtpData;
import com.ceyloncab.otpmgtservice.domain.entity.OtpEntity;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.otpmgtservice.domain.exception.DomainException;
import com.ceyloncab.otpmgtservice.domain.utils.Action;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.ceyloncab.otpmgtservice.domain.utils.UserRole;
import com.ceyloncab.otpmgtservice.external.repository.OtpRepository;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * This is service class for handle otp generation and validation business logic
 */
@Slf4j
@Service
public class OTPService {
    @Value("${otpservicemgt.attempt-count}")
    Integer attemptsCount;
    @Value("${otpservicemgt.request-count}")
    Integer otpRqtCount;
    @Value("${otpservicemgt.re-send-span}")
    Long reSendOtpSpan;
    @Value("${otpservicemgt.block-duration}")
    Long blockDuration;
    @Autowired
    SendSMSInterface sendSMSInterface;
    @Autowired
    OtpAlgorithmService otpAlgorithmService;
    @Autowired
    OtpRepository otpRepository;

    /**
     * generate random 6 digit otp
     *
     * @param request: otp userInfo
     * @return new otp
     */
    private String createNewEntryWithNewOTP(OtpGenerateRequest request) {

        Long generatedTime = new Date().getTime();
        // result[0]-otp, result[1]-secret
        String[] result = otpAlgorithmService.generateNewOTP();

        OtpEntity otpTransaction = new OtpEntity();
        otpTransaction.getActions().add(new OtpData(request.getAction(), 1, 0, generatedTime,generatedTime,0L,result[1]));
        otpTransaction.setMsisdn(request.getMsisdn());
        otpTransaction.setRole(getUserRoleFromChannel());
        otpRepository.save(otpTransaction);
        return result[0];
    }


    /**
     * Handle OTP generation process
     *
     * @param otpRequest: otp request details
     * @return OtpResponse
     */
    public CommonResponse otpGenerationService(OtpGenerateRequest otpRequest) {

        CommonResponse otpGenerationResponse = new CommonResponse<>();
        ResponseHeader responseHeader = new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS);
        otpGenerationResponse.setResponseHeader(responseHeader);
        Optional<OtpEntity> optionalOtpEntity;

        try {

            optionalOtpEntity = otpRepository.findOneByMsisdnAndRole(otpRequest.getMsisdn(), getUserRoleFromChannel());
            String newOtp = "";

            if (Boolean.FALSE.equals(optionalOtpEntity.isPresent())) {
                newOtp = createNewEntryWithNewOTP(otpRequest);
            } else {

                OtpEntity otpEntity = optionalOtpEntity.get();
                OtpData data = getOtpDataByAction(otpEntity,otpRequest.getAction());
                if(Objects.isNull(data)){
                    Long generatedTime = new Date().getTime();
                    // result[0]-otp, result[1]-secret
                    String[] result = otpAlgorithmService.generateNewOTP();
                    otpEntity.getActions().add(new OtpData(otpRequest.getAction(), 1, 0, generatedTime,generatedTime,0L,result[1]));
                    newOtp = result[0];
                }else {
                    Long currentTime = new Date().getTime();
                    Float timeDiff = (currentTime - data.getGeneratedTime()) / 60000.0f;
                    Long lastInitiateTime = data.getInitialedTime();
                    float spanDur = (currentTime - lastInitiateTime) / 60000.0f;
                    if (data.getVerifiedTime() != 0L) {
                        newOtp = generateNewOtpForExistingActionEntity(data,true);

                    } else if (data.getAttemptCount() >= attemptsCount || data.getRequestCount() >= otpRqtCount) {
                        if (timeDiff < blockDuration) {
                            responseHeader.setResponseCode("400");
                            responseHeader.setCode(Constants.OTP_CNT_EXCEEDED);
                            int difference = (int)Math.ceil(blockDuration - timeDiff);
                            responseHeader.setMessage("OTP count exceeded. Please retry after " + difference + " minutes");
                            return otpGenerationResponse;
                        }
                        //reset attempt count also
                        newOtp = generateNewOtpForExistingActionEntity(data,true);

                    } else if (spanDur < reSendOtpSpan) {
                        newOtp = generateNewOtpForExistingActionEntity(data, false);
                    } else {
                        newOtp = generateNewOtpForExistingActionEntity(data,true);
                    }
                }
                //save changes in otp entity
                otpRepository.save(otpEntity);
            }
            log.info("------------OTP-------------{}----{}", otpRequest.getMsisdn(), newOtp);

            //send OTP via SMS and get whether it delivered or not
            sendSMSInterface.sendOTP(otpRequest.getMsisdn(), newOtp);
            return otpGenerationResponse;
        } catch (Exception ex) {
            log.error("Otp generation domain level failure.Error:{}",ex.getMessage(),ex);
            throw new DomainException(Constants.ResponseData.OTP_GENERATION_FAILED);
        }
    }

    /**
     * Handle OTP validation process
     *
     * @param otpRequest: otp validation request details
     * @return OtpValidateResponse
     * @throws DomainException:
     */
    public CommonResponse otpValidationService(OtpValidateRequest otpRequest) throws DomainException {

        CommonResponse otpValidationResponse = new CommonResponse<>();
        ResponseHeader responseHeader = new ResponseHeader(Constants.ResponseData.VERIFY_SUCCESS);
        otpValidationResponse.setResponseHeader(responseHeader);
        Optional<OtpEntity> optionalOtpEntity;
        try {

            optionalOtpEntity = otpRepository.findOneByMsisdnAndRole(otpRequest.getMsisdn(), getUserRoleFromChannel());

            if (Boolean.FALSE.equals(optionalOtpEntity.isPresent())) {
                //return invalid otp validation response
                responseHeader.setResponseCode("400");
                responseHeader.setMessage("OTP not found");
                responseHeader.setCode(Constants.NOT_FOUND_CODE);
                return otpValidationResponse;
            }
            OtpEntity otpEntity = optionalOtpEntity.get();
            OtpData data = getOtpDataByAction(otpEntity,otpRequest.getAction());
            if (Objects.isNull(data)) {
                //return invalid otp validation response
                responseHeader.setResponseCode("400");
                responseHeader.setMessage("OTP not found");
                responseHeader.setCode(Constants.NOT_FOUND_CODE);
                return otpValidationResponse;
            }

            data.setAttemptCount(data.getAttemptCount() + 1);
            if (data.getAttemptCount() > attemptsCount) {
                //return exceed attempts count response
                responseHeader.setResponseCode("400");
                responseHeader.setCode(Constants.EXCEED_ATMPT_CNT_CODE);
                responseHeader.setMessage("Exceed attempts count");
                return otpValidationResponse;
            }

            String[] isValidOTPStr = otpAlgorithmService.isValidOTP(otpRequest.getOtp(), data.getOtpSecret(), data.getGeneratedTime());
            String validityDesc = isValidOTPStr[1];
            if ("false".equals(isValidOTPStr[0])) {
                //return invalid otp response
                otpRepository.save(otpEntity);
                responseHeader.setResponseCode("400");
                responseHeader.setCode(validityDesc.equals("OTP Not Matched") ? Constants.NOT_MATCH_CODE : Constants.EXPRE_CODE);
                responseHeader.setMessage(validityDesc);
                return otpValidationResponse;
            }

            if (data.getVerifiedTime() != 0L) {
                //return invalid otp response
                otpRepository.save(otpEntity);
                responseHeader.setResponseCode("400");
                responseHeader.setCode(Constants.ALRDY_VERIFYED_CODE);
                responseHeader.setMessage("Already verified OTP used");
                return otpValidationResponse;
            }
            //otp_transaction update verified time and reset counts
            data.setVerifiedTime(new Date().getTime());
            data.setAttemptCount(0);
            //save verified status in db
            otpRepository.save(otpEntity);
            return otpValidationResponse;

        } catch (Exception ex) {
            log.error("Otp validation domain level failure.Error:{}",ex.getMessage(),ex);
            throw new DomainException(Constants.ResponseData.OTP_VALIDATION_FAILED);
        }
    }

    private UserRole getUserRoleFromChannel(){
        UserRole userRole;
        Object channel = MDC.get(AopConstants.CHANNEL);
        if ("DRIVER".equals(channel)) {
            userRole = UserRole.DRIVER;
        }else {
            userRole = UserRole.CUSTOMER;
        }
        return userRole;
    }

    private String generateNewOtpForExistingActionEntity(OtpData data, Boolean isResetCount){
        Long generatedTime = new Date().getTime();
        // result[0]-otp, result[1]-secret
        String[] result = otpAlgorithmService.generateNewOTP();
        if(isResetCount){
            data.setRequestCount(1);
            data.setAttemptCount(0);
            data.setInitialedTime(generatedTime);
        }else {
            data.setRequestCount(data.getRequestCount()+1);
        }
        data.setGeneratedTime(generatedTime);
        data.setOtpSecret(result[1]);
        data.setVerifiedTime(0L);
        return result[0];
    }

    private OtpData getOtpDataByAction(OtpEntity entity, Action action){
        for (OtpData data : entity.getActions()) {
            if(data.getAction().equals(action)){
                return data;
            }
        }
        return null;
    }

}
