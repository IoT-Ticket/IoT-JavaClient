package com.iotticket.api.v1.model;

import java.util.List;

/**
 * Response from server after a write.
 */
public class WriteDataResponse {
    private List<WriteResult> writeResults;
    private int totalWritten;

    public List<WriteResult> getWriteResults() {
        return writeResults;
    }

    public void setWriteResults(List<WriteResult> writeResults) {
        this.writeResults = writeResults;
    }

    /**
     * @return Total number of data points written
     */
    public int getTotalWritten() {
        return totalWritten;
    }

    public void setTotalWritten(int totalWritten) {
        this.totalWritten = totalWritten;
    }

}
