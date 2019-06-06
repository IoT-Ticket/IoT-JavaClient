package com.iotticket.api.v1.model;


public class DeviceQuota {

    private Long totalRequestToday;
    private Integer maxReadRequestPerDay;
    private String deviceId;
    private Long storageSize;
    private Integer numberOfDataNodes;

    /**
     * @return Total number of request made to the API to the device.
     * Any request url that includes the device Id is added to this count.
     */
    public Long getTotalRequestToday() {
        return totalRequestToday;
    }

    public void setTotalRequestToday(Long totalRequestToday) {
        this.totalRequestToday = totalRequestToday;
    }

    /**
     * @return The max number of read request allowed to the client for this device.
     */
    public Integer getMaxReadRequestPerDay() {
        return maxReadRequestPerDay;
    }

    public void setMaxReadRequestPerDay(Integer maxReadRequestPerDay) {
        this.maxReadRequestPerDay = maxReadRequestPerDay;
    }

    /**
     * @return The device id for which this DeviceQuota belongs to.
     */
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    /**
     * @return The total size in bytes that the client has written to the server for the specified device.
     */
    public Long getStorageSize() {
        return storageSize;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize = storageSize;
    }


    /**
     * @return The number of data node created for the specified device.
     */
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