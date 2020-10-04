package com.iotticket.api.v1.model;

import com.iotticket.api.v1.model.DatanodeQueryCriteria.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains various attributes that can be used to query statistical data
 * from server
 * <p>
 * The deviceId, grouping, fromDate, toDate and the names or full path of the datanodes
 * to be queried must be supplied.
 * </p>
 */

public class StatisticalDataQueryCriteria {

    private String deviceId;
    private Order sortOrder;
    private Long fromDate;
    private Long toDate;
    private Set<String> dataPaths = new HashSet<String>();
    private Grouping grouping;
    private Set<String> vtags = new HashSet<String>();

    public StatisticalDataQueryCriteria(String deviceId, Grouping grouping, Date fromDate, Date toDate, String... datapoints) {
        setDeviceId(deviceId);
        setDataPaths(Arrays.asList(datapoints));
        setGrouping(grouping);
        setFromDate(fromDate);
        setToDate(toDate);
    }

    public StatisticalDataQueryCriteria(String deviceId, Grouping grouping, long fromDate, long toDate, String... datapoints) {
        setDeviceId(deviceId);
        setDataPaths(Arrays.asList(datapoints));
        setGrouping(grouping);
        setFromDate(fromDate);
        setToDate(toDate);
    }

    public Long getFromDate() {
        return fromDate;
    }


    /**
     * Unix Timestamp. Number of milliseconds since the Epoch.
     * Defines the begining time from which the process values are fetched.
     *
     * @param fromDate
     */

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public void setFromDate(Date fromDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("fromDate must be set");
        }
    }

    public Long getToDate() {
        return toDate;
    }

    /**
     * @param toDate Unix Timestamp. Number of milliseconds since the Epoch.
     *               Defines the ending time from which the process values are fetched.
     */

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public void setToDate(Date toDate) {
        if (toDate == null) {
            throw new IllegalArgumentException("toDate must be set");
        }
    }

    public String getDeviceId() {
        return deviceId;
    }

    /**
     * @param deviceId The id of the device to be queried.
     */
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

    public String getDataPathsAsString() {
        return getUrlParameterString(getDataPaths());
    }
    
    private String getUrlParameterString(Collection<String> collection) {
        // Create a StringBuilder with as big capacity as needed to hold the result.
        final int GUESSED_STRING_LENGTH = 20;
        StringBuilder sb = new StringBuilder(collection.size() * GUESSED_STRING_LENGTH);
        // Append all strings to the StringBuilder
        for (String s : collection) {
            sb.append(s);
            sb.append(',');
        }
    }

    public Set<String> getDataPaths() {
        return dataPaths;
    }

    /**
     * Example for a device that has only these three datanodes.
     * <p>
     * <pre>
     *           NAME     |  PATH
     * 1.    Temperature  |
     * 2.    Temperature  | Engine/Core
     * 3.    Temperature  | Engine/Auxilliary
     * </pre>
     * </p>
     * To query specifically for the first,  the dataPath will be "/Temperature" <br/>
     * To query specifically for the second, the dataPath will be "/Engine/Core/Temperature"<br/>
     * To query specifically for the third,  the dataPath will be "/Engine/Auxilliary/Temperature"<br/>
     * <p/>
     * To make a combined operation to read the process values for all three datanodes, the three dataPaths above
     * can be added to the param dataPaths. Alternatively, the dataPath "Temperature" without the slash could be used
     * as they all have the name "Temperature".
     * <p/>
     * <p/>
     * NOTE A query using /Engine or /Engine/core as a dataPath will not return any result.
     * </P>
     *
     * @param dataPaths Collection of fullpaths or datanode name.
     */
    public void setDataPaths(Collection<String> dataPaths) {
        if (dataPaths.isEmpty()) {
            throw new IllegalArgumentException("At least one datapoint needs to be defined");
        }

        this.dataPaths.clear();
        this.dataPaths.addAll(dataPaths);
    }

    public Grouping getGrouping() {
        return grouping;
    }

    /**
     * Determines the grouping type for the statistical data.
     *
     * @param grouping
     */
    public void setGrouping(Grouping grouping) {
        if (grouping == null) {
            throw new IllegalArgumentException("Grouping must be set");
        }
        this.grouping = grouping;
    }

    /**
     * @return Returns a string that contains vtags separated with
     * comma.
     */
    public String getVtagsAsString() {
        return getUrlParameterString(getVtags());
    }

    public Set<String> getVtags() {
        return vtags;
    }

    public void setVtags(Set<String> vtags) {
        this.vtags = vtags;
    }

    public enum Grouping {
        Minute,
        Hour,
        Day,
        Week,
        Month,
        Year
    }
}
