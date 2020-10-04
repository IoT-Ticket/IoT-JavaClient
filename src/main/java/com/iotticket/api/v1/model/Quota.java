package com.iotticket.api.v1.model;

public class Quota {
    public static int UNLIMITED = -1;
    private Integer totalDevices;
    private Integer maxNumberOfDevices;
    private Integer maxDataNodePerDevice;
    private Long usedStorageSize;
    private Long maxStorageSize;

    /**
     * @return Total number of devices the client owns.
     * NOTE: This is not the same as the total number of devices the client has access to.
     */
    public Integer getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Integer totalDevices) {
        this.totalDevices = totalDevices;
    }


    /**
     * @return The maximum number of devices the client can create.
     */
    public Integer getMaxNumberOfDevices() {
        return maxNumberOfDevices;
    }

    public void setMaxNumberOfDevices(Integer maxNumberOfDevices) {
        this.maxNumberOfDevices = maxNumberOfDevices;
    }

    /**
     * @return The maximum datanodes per device allowed for a client.
     */

    public Integer getMaxDataNodePerDevice() {
        return maxDataNodePerDevice;
    }

    public void setMaxDataNodePerDevice(Integer maxDataNodePerDevice) {
        this.maxDataNodePerDevice = maxDataNodePerDevice;
    }


    /**
     * @return The total size in bytes that the client has written to the server.
     */
    public Long getUsedStorageSize() {
        return usedStorageSize;
    }

    public void setUsedStorageSize(Long usedStorageSize) {
        this.usedStorageSize = usedStorageSize;
    }

    /**
     * @return The maximum size in bytes that the client has a right to write to the server.
     */
    public Long getMaxStorageSize() {
        return maxStorageSize;
    }

    public void setMaxStorageSize(Long maxStorageSize) {
        this.maxStorageSize = maxStorageSize;
    }

    @Override
    public String toString() {
        return "Quota{" +
                "totalDevices=" + totalDevices +
                ", maxNumberOfDevices=" + maxNumberOfDevices +
                ", maxDataNodePerDevice=" + maxDataNodePerDevice +
                ", usedStorageSize=" + usedStorageSize +
                ", maxStorageSize=" + maxStorageSize +
                '}';
    }


}
