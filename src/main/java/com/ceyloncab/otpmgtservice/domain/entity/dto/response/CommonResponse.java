package com.ceyloncab.otpmgtservice.domain.entity.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    private T data;
    private ResponseHeader responseHeader;
}
