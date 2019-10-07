package com.iotticket.api.v1.model;

import java.net.URI;

public class WriteResult {
    private URI href;
    private int writtenCount;


    /**
     * @return URI to read from the data node targeted in the write.
     */
    public URI getHref() {
        return href;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    /**
     * @return The number of values written to the particular data node
     */
    public int getWrittenCount() {
        return writtenCount;
    }

    public void setWrittenCount(int writtenCount) {
        this.writtenCount = writtenCount;
    }

}
