package com.ceyloncab.otpmgtservice.domain.utils;

import lombok.Getter;

public class Constants {
    public static final String UNHANDLED_ERROR_CODE = "OTP3000";
    public static final String OTP_CNT_EXCEEDED = "OTP2002";
    public static final String NOT_FOUND_CODE = "OTP2003";
    public static final String EXCEED_ATMPT_CNT_CODE = "OTP2004";
    public static final String NOT_MATCH_CODE = "OTP2005";
    public static final String EXPRE_CODE = "OTP2006";
    public static final String ALRDY_VERIFYED_CODE = "OTP2007";

    @Getter
    public enum ResponseData {
        COMMON_SUCCESS("OTP1000", "Success","200"),
        VERIFY_SUCCESS("OTP1001", "Verified","200"),
        COMMON_FAIL("OTP2000", "Failed","400"),
        OTP_GENERATION_FAILED("OTP2001","Otp generation failed. Try-again later","500"),
        OTP_VALIDATION_FAILED("OTP2002","Otp validation failed. Try-again later","500"),
        INTERNAL_SERVER_ERROR("OTP3001", "Internal Server Error","500");

        private final String code;
        private final String message;
        private final String responseCode;

        ResponseData(String code, String message, String responseCode) {
            this.code = code;
            this.message = message;
            this.responseCode= responseCode;
        }
    }
}
