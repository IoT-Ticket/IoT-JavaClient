package com.iotticket.api.v1.model;


public class DeviceQuota {

    private Long totalRequestToday;
    private Integer maxReadRequestPerDay;
    private String deviceId;
    private Long storageSize;
    private Integer numberOfDataNodes;

    public Long getTotalRequestToday() {
        return totalRequestToday;
    }

    public void setTotalRequestToday(Long totalRequestToday) {
        this.totalRequestToday = totalRequestToday;
    }

    public Integer getMaxReadRequestPerDay() {
        return maxReadRequestPerDay;
    }

    public void setMaxReadRequestPerDay(Integer maxReadRequestPerDay) {
        this.maxReadRequestPerDay = maxReadRequestPerDay;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }

    public Integer getNumberOfDataNodes() {
        return numberOfDataNodes;
    }

    public void setNumberOfDataNodes(Integer numberOfDataNodes) {
        this.numberOfDataNodes = numberOfDataNodes;
    }

    @Override
    public String toString() {
        return "DeviceQuota{" +
                "totalRequestToday=" + totalRequestToday +
                ", maxReadRequestPerDay=" + maxReadRequestPerDay +
                ", deviceId='" + deviceId + '\'' +
                ", storageSize=" + storageSize +
                ", numberOfDataNodes=" + numberOfDataNodes +
                '}';
    }


}