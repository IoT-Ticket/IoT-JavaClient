package com.iotticket.api.v1.exception;


import com.iotticket.api.v1.model.ErrorInfo;

public class IoTServerCommunicationException extends RuntimeException {


    private static final long serialVersionUID = -3510853523188440984L;
    private final ErrorInfo errorInfo;


    public IoTServerCommunicationException(String message, ErrorInfo errorInfo) {

        super(message);
        this.errorInfo = errorInfo;
    }


    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
