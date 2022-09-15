package com.blog.api.exception;

public class InvalidRequestException extends ApplicationException{

    private static final String MESSAGE = "정상적인 요청이 아닙니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }

    public InvalidRequestException(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
