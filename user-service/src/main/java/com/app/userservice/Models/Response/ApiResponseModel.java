package com.app.userservice.Models.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseModel<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ApiResponseModel<T> success(T data) {
        return new ApiResponseModel<>(true, "Operation completed successfully", data);
    }

    public static <T> ApiResponseModel<T> success(String message, T data) {
        return new ApiResponseModel<>(true, message, data);
    }

    public static <T> ApiResponseModel<T> error(String message) {
        return new ApiResponseModel<>(false, message, null);
    }
} 