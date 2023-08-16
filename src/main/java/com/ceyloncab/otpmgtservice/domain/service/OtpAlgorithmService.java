package com.ceyloncab.otpmgtservice.domain.service;

import com.ceyloncab.otpmgtservice.domain.exception.DomainException;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;


/**
 * This is external service class for generate otp based on time and validate(use HMAC algorithm and Timebased otp generation process)
 *
 */
@Slf4j
@Service
public class OtpAlgorithmService {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    @Value("${otpservicemgt.expire-time}")
    Long otpExpTimeMin;


    // to generate a secret key
    public String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }

    public byte[] generateHashKey(String secret, String counter) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(counter.getBytes());
    }

    //generate 6digit code
    private String generateHotp(byte[] hash) {

        int offset = hash[19] & 0xF;
        long truncatedHash = hash[offset] & 0x7F;
        for (int the = 1; the < 4; the++) {
            // Perform the shift left 8 bits
            truncatedHash <<= 8;
            // Get the next element of the sequence, and performing a bitwise and with
            // 00000000 00000000 00000000 00000000 00000000 00000000 00000000 11111111
            truncatedHash |= hash[offset + the] & 0xFF;
        }

        String truncValue = String.valueOf(truncatedHash);
        // Return the 6 digit
        return truncValue.substring(truncValue.length() - 4);

    }

    /**
     * generate new otp and related otpSecretKey(Which also contains expire time)
     * @return otp(6 - digit) amd corresponding secret key
     */
    public String[] generateNewOTP() {
        try {
            // otp generation with counter value equals 0
            String counter = String.valueOf(0);
            String secret = generateSecretKey();
            byte[] hash = generateHashKey(secret, counter);
            return new String[]{generateHotp(hash), secret};
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Error occurred in otp generation.Error:{}",ex.getMessage(),ex);
            throw new DomainException(Constants.ResponseData.OTP_GENERATION_FAILED);
        }
    }


    /**
     * check whether is otp not expired and equal
     *
     * @param submittedOtp:      user send otp
     * @param otpSecretKey: stored otpSecretKey
     * @return true or false
     */
    public String[] isValidOTP(String submittedOtp, String otpSecretKey, long generatedTime){
        String counter = String.valueOf((new Date().getTime() - generatedTime) / (otpExpTimeMin * 60000));
        try {
            if (!counter.equals("0")) {
                return new String[]{"false", "OTP Expired"};
            }
            byte[] hashForValidation = generateHashKey(otpSecretKey, counter);
            String otpForValidation = generateHotp(hashForValidation);
            boolean validity = otpForValidation.equals(submittedOtp);
            return new String[]{String.valueOf(validity), validity ? "Success" : "OTP Not Matched"};

        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            log.error("Error occurred validating otp.Error:{}",ex.getMessage(),ex);
            throw new DomainException(Constants.ResponseData.OTP_VALIDATION_FAILED);
        }
    }
}
