package com.ceyloncab.otpmgtservice.application.exception;

import com.ceyloncab.otpmgtservice.application.controller.BaseController;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.otpmgtservice.domain.exception.DomainException;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.ceyloncab.otpmgtservice.external.exception.ExternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController extends BaseController {
    @ExceptionHandler(value = {DomainException.class,ExternalException.class})
    public ResponseEntity<Object> genericException(RuntimeException exception) {
        ResponseHeader response = createResponse(exception);
        return getResponseEntity(response.getResponseCode(),response);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> unhandledException(RuntimeException exception) {
        ResponseHeader response = new ResponseHeader();
        response.setCode(Constants.UNHANDLED_ERROR_CODE);
        response.setMessage(exception.getMessage());
        response.setResponseCode("500");
        return getResponseEntity(response.getResponseCode(),response);
    }
    private ResponseHeader createResponse(RuntimeException exception){
        ResponseHeader response = new ResponseHeader(Constants.ResponseData.INTERNAL_SERVER_ERROR);
        if(exception instanceof DomainException){
            DomainException de = (DomainException) exception;
            response.setCode(de.getCode());
            response.setMessage(de.getMessage());
            response.setResponseCode(de.getResponseCode());
        } else if (exception instanceof ExternalException) {
            ExternalException ext = (ExternalException) exception;
            response.setCode(ext.getCode());
            response.setMessage(ext.getMessage());
            response.setResponseCode(ext.getResponseCode());
        }
        return response;
    }
}
