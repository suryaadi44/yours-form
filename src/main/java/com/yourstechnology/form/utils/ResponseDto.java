package com.yourstechnology.form.utils;

import lombok.Data;

@Data
public class ResponseDto<T> {
    private String message;
    private Object errors;
    private T data;
}
