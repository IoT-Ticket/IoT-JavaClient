package com.iotticket.api.v1.model;

import javax.ws.rs.core.Response.StatusType;

public class ErrorInfo {

    public String description;
    public int code;
    public String moreInfo;
    public int apiver;
    private StatusType httpStatus;

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