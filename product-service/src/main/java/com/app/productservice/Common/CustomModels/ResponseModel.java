package com.app.productservice.Common.CustomModels;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel {
    public HttpStatus status;
    public String message;
    public Object data;
}