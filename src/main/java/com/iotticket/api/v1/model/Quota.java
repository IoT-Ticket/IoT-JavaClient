package com.iotticket.api.v1.model;

public class Quota {
    public static int UNLIMITED = -1;
    private Integer totalDevices;
    private Integer maxNumberOfDevices;
    private Integer maxDataNodePerDevice;
    private Long usedStorageSize;
    private Long maxStorageSize;

    public Integer getTotalDevices() {
        return totalDevices;
    }

    public void setTotalDevices(Integer totalDevices) {
        this.totalDevices = totalDevices;
    }

    public Integer getMaxNumberOfDevices() {
        return maxNumberOfDevices;
    }

    public void setMaxNumberOfDevices(Integer maxNumberOfDevices) {
        this.maxNumberOfDevices = maxNumberOfDevices;
    }

    public Integer getMaxDataNodePerDevice() {
        return maxDataNodePerDevice;
    }

    public void setMaxDataNodePerDevice(Integer maxDataNodePerDevice) {
        this.maxDataNodePerDevice = maxDataNodePerDevice;
    }

    public Long getUsedStorageSize() {
        return usedStorageSize;
    }

    public void setUsedStorageSize(Long usedStorageSize) {
        this.usedStorageSize = usedStorageSize;
    }

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
