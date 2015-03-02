package com.iotticket.api.v1.exception;


public class ValidAPIParamException extends Exception {

    private static final long serialVersionUID = 3542063386068095441L;

    public ValidAPIParamException(String message) {
        super(message);
    }
}
