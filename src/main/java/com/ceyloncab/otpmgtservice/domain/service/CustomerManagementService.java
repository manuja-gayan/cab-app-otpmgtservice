package com.ceyloncab.otpmgtservice.domain.service;

import com.ceyloncab.otpmgtservice.application.transport.request.CustomerRequest;
import com.ceyloncab.otpmgtservice.domain.entity.CustomerEntity;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.CommonResponse;
import com.ceyloncab.otpmgtservice.domain.entity.dto.response.ResponseHeader;
import com.ceyloncab.otpmgtservice.domain.service.assembler.impl.CreateCustomerAssembler;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.ceyloncab.otpmgtservice.external.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagementService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CreateCustomerAssembler createCustomerAssembler;
    public CommonResponse<CustomerRequest> createCustomerProfile(CustomerRequest request) {

        CommonResponse<CustomerRequest> response = new CommonResponse<>();
        CustomerEntity document = createCustomerAssembler.fromDto(request);
        try {
            document = customerRepository.save(document);
        } catch (Exception ex) {
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }
        response.setData(createCustomerAssembler.toDto(document));
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS));
        return response;
    }
    public CommonResponse<CustomerRequest>updateCustomerProfile(CustomerRequest request){
        CommonResponse<CustomerRequest> response = new CommonResponse<>();
        ResponseHeader header;


       /* CustomerRequest customerRequest = createCustomerAssembler.toDto(customerRepository.findById(request.getUserId()).get());
        CustomerEntity document = createCustomerAssembler.fromDto(customerRequest);*/
        CustomerEntity customerEntity = customerRepository.findById(request.getUserId()).get();
        try {

            customerEntity.setFirstName(request.getFirstName());
            customerEntity.setLastName(request.getLastName());
            customerEntity.setLocation(request.getLocation());
            customerEntity.setMsisdn(request.getMsisdn());
//            customerEntity.setUserId(request.getUserId());
            customerEntity = customerRepository.save(customerEntity);
        }catch (Exception ex){
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }

        response.setData(createCustomerAssembler.toDto(customerEntity));
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS));
        return response;

    }

    public CommonResponse<CustomerRequest> getCustomerProfile(String userId) {
        CommonResponse<CustomerRequest> response = new CommonResponse<>();
        if (userId.equals("none")) {
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }
        CustomerEntity byId;
        try {
            byId = customerRepository.findById(userId).get();
            response.setData(createCustomerAssembler.toDto(byId));
        } catch (Exception e) {
            response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_FAIL));
            return response;
        }
        response.setResponseHeader(new ResponseHeader(Constants.ResponseData.COMMON_SUCCESS));
        return response;
    }
}
