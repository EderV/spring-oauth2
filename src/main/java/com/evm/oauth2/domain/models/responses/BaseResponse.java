package com.evm.oauth2.domain.models.responses;

import lombok.Data;

@Data
public class BaseResponse<T> {

    private T data;
    private String errorMsg;
    private Throwable throwable;

}
