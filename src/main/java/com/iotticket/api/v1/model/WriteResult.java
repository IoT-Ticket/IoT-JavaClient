package com.iotticket.api.v1.model;

public class WriteResult {
    private String href;
    private int writtenCount;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getWrittenCount() {
        return writtenCount;
    }

    public void setWrittenCount(int writtenCount) {
        this.writtenCount = writtenCount;
    }

}
