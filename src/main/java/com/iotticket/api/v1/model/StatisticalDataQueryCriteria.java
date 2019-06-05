package com.iotticket.api.v1.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iotticket.api.v1.model.DatanodeQueryCriteria.Order;

/**
 * This class contains various attributes that can be used to query statistical data
 * from server
 * <p>
 * The deviceId, grouping, fromDate, toDate and the names or full path of the datanodes
 * to be queried most be supplied
 * </p>
 */

// TODO: Most of the code is currently just copied from DataNodeQueryCriteria.
// Maybe do some refactoring to existing code to avoid duplicate code.
public class StatisticalDataQueryCriteria {

	private String deviceId;
    private Order sortOrder;
    private Long fromDate;
    private Long toDate;
    private Set<String> dataPaths = new HashSet<String>();
    private Grouping grouping;
    
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

    /**
     *
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
    
	public void setFromDate(Date fromDate) {
		if (fromDate == null) {
            throw new IllegalArgumentException("fromDate must be set");
		}
	}
	
	public void setToDate(Date toDate) {
		if (toDate == null) {
            throw new IllegalArgumentException("toDate must be set");
		}
	}
	
	/**
	 * Determines the grouping type for the statistical data.
	 * @param grouping
	 */
	
	public void setGrouping(Grouping grouping) {
		if (grouping == null) {
			throw new IllegalArgumentException("Grouping must be set");
		}
	}
	
	public Grouping getGrouping() {
		return grouping;
	}
	
	// TODO: vtags not included in DatanodeQueryCriteria so should we also leave 
	// them out from here?
	
	public static enum Grouping {
		Minute,
		Hour,
		Day,
		Week,
		Month,
		Year
	}
	
}
