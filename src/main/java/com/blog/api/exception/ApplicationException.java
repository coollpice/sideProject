package com.blog.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 최상위 Exception 클래스. application 에서 만든 모든 CustomException 들이 상속해야한다.
 */
@Getter
public abstract class ApplicationException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addValidation(String fieldName, String message) {
        this.validation.put(fieldName, message);
    }

    public abstract int getStatusCode();

}
