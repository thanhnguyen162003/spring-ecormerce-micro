package com.app.productservice.Common.CustomModels;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ResponseModel {
    public String message;
    public Object data;
    public HttpStatus status;
}