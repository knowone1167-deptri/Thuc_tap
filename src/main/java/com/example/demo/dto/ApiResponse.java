package com.example.demo.dto;

public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // Static factory methods

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Request successful", data);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    // Getter & Setter

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }
}// hoan thanh api
