package com.iotticket.api.v1.model;

import javax.ws.rs.core.Response.StatusType;

public class ErrorInfo {

    /**
     * This field provides a general description of the error.
     */
    public String description;

    /**
     * The IOT-API server specific code for the error.
     */
    public int code;

    /**
     * This field points to the documentation url where more description about the error code can be found.
     */
    public String moreInfo;

    /**
     * The api version number.
     */
    public int apiver;

    private StatusType httpStatus;

    /**
     * @return Actual HttpStatus returned from server
     */
    public StatusType getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(StatusType httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" {\"");
        if (description != null) {
            builder.append("description\":\"");
            builder.append(description);
            builder.append("\",\"");
        }
        builder.append("code\":");
        builder.append(code);
        builder.append(",\"");
        if (moreInfo != null) {
            builder.append("moreInfo\":\"");
            builder.append(moreInfo);
            builder.append("\",\"");
        }
        builder.append("apiver\":");
        builder.append(apiver);
        builder.append("}");
        return builder.toString();
    }


}