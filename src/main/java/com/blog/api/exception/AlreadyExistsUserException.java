package com.blog.api.exception;

public class AlreadyExistsUserException extends ApplicationException{

    private static final String MESSAGE = "이미 가입된 회원입니다.";

    public AlreadyExistsUserException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
