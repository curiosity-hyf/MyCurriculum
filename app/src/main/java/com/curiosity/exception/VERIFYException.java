package com.curiosity.exception;

/**
 * Created by Curiosity on 2016-9-17.
 */
public class VerifyException extends Exception{

    public VerifyException() {
        super("验证码错误!");
    }
}
