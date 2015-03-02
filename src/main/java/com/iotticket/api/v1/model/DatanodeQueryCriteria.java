package com.iotticket.api.v1.model;

import java.util.*;

public class DatanodeQueryCriteria {

    private String deviceId;
    private Order sortOrder;
    private Integer limit;
    private Long fromDate;
    private Long toDate;
    private Set<String> dataPaths = new HashSet<String>();

    public DatanodeQueryCriteria(String deviceId, String... datapoints) {
        List<String> datanodePoints = Arrays.asList(datapoints);
        setDeviceId(deviceId);
        setDataPaths(datanodePoints);

    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date date) {
        this.fromDate = date.getTime();
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Date date) {
        this.toDate = date.getTime();
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        if (deviceId == null || deviceId.isEmpty())
            throw new IllegalArgumentException("DeviceId must be set");
        this.deviceId = deviceId;
    }

    public Order getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Order sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getDataNodePaths() {
        StringBuilder sb = new StringBuilder();

        String[] strings = dataPaths.toArray(new String[dataPaths.size()]);
        int len = strings.length;
        for (int i = 0; i < len; i++) {
            sb.append(strings[i]);
            if (i != (len - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public Set<String> getDataPaths() {
        return dataPaths;
    }

    public void setDataPaths(Collection<String> dataPaths) {
        if (dataPaths.isEmpty()) {
            throw new IllegalArgumentException("At least one datapoint needs to be defined");
        }
        this.dataPaths.clear();
        this.dataPaths.addAll(dataPaths);

    }

    public static enum Order {
        Ascending,
        Descending
    }


}