package com.app.lika.exception;

public enum ExceptionCustomCode {
    //general code

    //Authentication code
    USERNAME_ALREADY_EXISTS(2000),
    EMAIL_ALREADY_EXISTS(2001),
    USERNAME_NOT_FOUND(2003),
    INCORRECT_PASSWORD(2004),
    ACCOUNT_LOCKED(2005),
    JWT_TOKEN_EXPIRED(2006),
    QUESTION_BANK_IS_NOT_ENOUGH(2007);


    private int code;

    ExceptionCustomCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
