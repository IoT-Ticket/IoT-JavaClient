package com.iotticket.api.v1;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.*;
import com.iotticket.api.v1.model.Datanode.DataNodeList;
import com.iotticket.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.api.v1.model.Datanode.DatanodeReadValue;
import com.iotticket.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.api.v1.model.Device.DeviceDetails;
import com.iotticket.api.v1.validation.ValidationRunner;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.StatusType;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOTAPIClient {


    private final static String DevicesResource = "devices/";
    private final static String SpecificDeviceResourceFormat = DevicesResource + "{deviceId}/";
    private final static String DatanodesResourceFormat = SpecificDeviceResourceFormat + "datanodes/";
    private final static String WriteDataResourceFormat = "process/write/{deviceId}/";
    private final static String ReadDataResourceFormat = "process/read/{deviceId}/";
    private final static String ReadStatisticalDataResourceFormat = "stat/read/{deviceId}";
    private final static String QuotaAllResource = "quota/all/";
    private final static String QuotaDeviceResourceFormat = "quota/{deviceId}/";
    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
    private static final Logger LOG = Logger.getLogger(IOTAPIClient.class.getName());
    private static final MediaType JSON = MediaType.APPLICATION_JSON_TYPE;
    private final WebTarget baseTarget;
    private final ValidationRunner validator = new ValidationRunner();


    public IOTAPIClient(String serverUrl, String userName, String password) {
        if (serverUrl == null || userName == null || password == null) {
            throw new IllegalArgumentException("Ensure the serverUrl, username and password are not null");
        }
        Client client = ClientBuilder.newClient().register(HttpAuthenticationFeature.basic(userName, password));
        baseTarget = client.target(serverUrl);


    }


    /**
     * @param device provides the description of the device to be registered.
     * @return a <tt>DeviceDetails</tt> object that includes, among other things specified in the device object, the deviceId
     * {@link DeviceDetails#getDeviceId}.and the URI for the newly registered device.
     * @throws ValidAPIParamException if the device does not meet the specified requirements
     */

    public DeviceDetails registerDevice(Device device) throws ValidAPIParamException {

        validator.runValidation(device);

        String json = gson.toJson(device);
        Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON + "; charset=UTF-8");
        Response res = baseTarget.path(DevicesResource).request(JSON).post(entity);
        return getResponse(res, DeviceDetails.class);
    }


    private <T> T getResponse(Response res, Class<T> clz) {

        StatusType statusInfo = res.getStatusInfo();
        String jsonResponse = res.readEntity(String.class);
        LOG.finest(jsonResponse);
        if (statusInfo.getFamily() != Response.Status.OK.getFamily()) {

            ErrorInfo errorResponse = getErrorInfo(statusInfo, jsonResponse);
            LOG.info(errorResponse.toString());
            throw new IoTServerCommunicationException("Request with server was unsuccesful, check the errorInfo object for further details", errorResponse);

        }
        return gson.fromJson(jsonResponse, clz);
    }

    private ErrorInfo getErrorInfo(StatusType status, String jsonResponse) {
        ErrorInfo info = new ErrorInfo();
        info.setHttpStatus(status);
        try {
            info = gson.fromJson(jsonResponse, ErrorInfo.class);
            info.setHttpStatus(status);
            return info;
        } catch (JsonSyntaxException e) {
            LOG.log(Level.FINE, "Error info was not obtained from the server. Status was " + status, e);
        }

        return info;

    }

    public WriteDataResponse writeData(String deviceId, DatanodeWriteValue... data) throws ValidAPIParamException {
        return writeData(deviceId, Arrays.asList(data));
    }


    /**
     *
     * @param deviceId The target Device identifier
     * @param writeValues Collection of DatanodeWriteValue to write to the server
     * @return <tt>WriteDataResponse</tt>
     * @throws ValidAPIParamException If the any of the DatanodeWriteValue doesn't meet specified requirement.
     */

    public WriteDataResponse writeData(String deviceId, Collection<DatanodeWriteValue> writeValues) throws ValidAPIParamException {

        for (DatanodeWriteValue writeValue : writeValues) {
            validator.runValidation(writeValue);
        }

        String json = gson.toJson(writeValues);

        LOG.finest(json);
        Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON + "; charset=UTF-8");

        WebTarget target = baseTarget.path(WriteDataResourceFormat).resolveTemplate("deviceId", deviceId);
        Response res = target.request(JSON).post(entity);
        return getResponse(res, WriteDataResponse.class);
    }


    /**
     * @param offset The amount to skip from the beginning.
     * @param limit  The maximum amount of result to be returned
     * @return a Collection of client's devices with paging support. Obtain items list using {@link PagedResult#getResults}
     */

    public PagedResult<DeviceDetails> getDeviceList(int offset, int limit) {


        WebTarget target = baseTarget.path(DevicesResource).queryParam("limit", limit).queryParam("offset", offset);
        Response res = target.request().accept(JSON).get();
        return getResponse(res, Device.DeviceList.class);


    }

    /**
     *
     * @param deviceId The device to query.
     * @param offset The amount to skip from the beginning.
     * @param limit  The maximum amount of result to be returned
     * @return Get a list of provided device datanodes with Paging support.Obtain items list using {@link PagedResult#getResults}
     */
    public PagedResult<Datanode> getDeviceDataNodeList(String deviceId, int offset, int limit) {

        WebTarget target = baseTarget.path(DatanodesResourceFormat).resolveTemplate("deviceId", deviceId).queryParam("limit", limit).queryParam("offset", offset);
        Response res = target.request().accept(JSON).get();
        return getResponse(res, DataNodeList.class);

    }

    /**
     * @param deviceId The deviceId for the device to be fetched from the server.
     * @return <tt>DeviceDetails</tt> that describes the device's name, attributes, URI etc.
     */

    public Device.DeviceDetails getDevice(String deviceId) {
        WebTarget target = baseTarget.path(SpecificDeviceResourceFormat).resolveTemplate("deviceId", deviceId);
        Response res = target.request().accept(JSON).get();
        return getResponse(res, DeviceDetails.class);
    }


    /**
     * @param criteria The <tt>DatanodeQueryCriteria</tt> object used to query for the process values
     * @return Request <tt>ProcessValues</tt>
     * @See DatanodeQueryCriteria
     */
    public ProcessValues readProcessData(DatanodeQueryCriteria criteria) {


        WebTarget target = baseTarget.path(ReadDataResourceFormat);
        target = target.resolveTemplate("deviceId", criteria.getDeviceId());
        target = target.queryParam("datanodes", criteria.getDataNodePaths());

        if (criteria.getSortOrder() != null) {
            target = target.queryParam("order", criteria.getSortOrder().name());
        }
        if (criteria.getLimit() != null) {
            target = target.queryParam("limit", criteria.getLimit());
        }

        if (criteria.getFromDate() != null) {
            target = target.queryParam("fromdate", criteria.getFromDate());
        }

        if (criteria.getToDate() != null) {
            target = target.queryParam("todate", criteria.getToDate());
        }

        Response res = target.request().accept(JSON).get();

        ProcessValues pv = getResponse(res, ProcessValues.class);
        Collection<DatanodeRead> datanodeReads = pv.getDatanodeReads();
        for (DatanodeRead datanodeRead : datanodeReads) {
            DataType dataType = datanodeRead.getDataType();
            Collection<DatanodeReadValue> values = datanodeRead.getDatanodeReadValues();

            for (DatanodeReadValue v : values) {
                v.setDataType(dataType);
            }
        }
        return pv;

    }
    
    /**
     * Note that minimum, average and maximum might be <b>null</b> in <tt>StatisticalDataReadValue</tt> if time interval
     * contained no suitable values for calculating statistics.
     * @param criteria The <tt>StatisticalDataQueryCriteria</tt> object used to query for the statistical values
     * @return Statistical data returned as <tt>StatisticalDataValues</tt> object
     * @See StatisticalDataQueryCriteria
     */
    public StatisticalDataValues readStatisticalData(StatisticalDataQueryCriteria criteria) {
        WebTarget target = baseTarget.path(ReadStatisticalDataResourceFormat);
        
        target = target.resolveTemplate("deviceId", criteria.getDeviceId());
        target = target.queryParam("datanodes", criteria.getDataPathsAsString());
        target = target.queryParam("grouping", criteria.getGrouping().name());
        target = target.queryParam("fromdate", criteria.getFromDate());
        target = target.queryParam("todate", criteria.getToDate());
        
        if (criteria.getSortOrder() != null) {
            target = target.queryParam("order", criteria.getSortOrder().name());
        }
        
        if (!criteria.getVtags().isEmpty()) {
            target = target.queryParam("vtags", criteria.getVtagsAsString());
        }
        
        Response res = target.request().accept(JSON).get();
        
        StatisticalDataValues values = getResponse(res, StatisticalDataValues.class);
        return values;
    }
    
    
    /**
     *
     * @return Fetches the user's <tt>Quota</tt> information from the server
     */
    public Quota getQuota() {
        Response res = baseTarget.path(QuotaAllResource).request().accept(JSON).get();
        return getResponse(res, Quota.class);
    }


    /**
     *
     * @param deviceId
     * @return Get the <tt>DeviceQuota</tt> from the server for the device with the specified deviceId
     */

    public DeviceQuota getDeviceQuota(String deviceId) {
        Response res = baseTarget.path(QuotaDeviceResourceFormat).resolveTemplate("deviceId", deviceId).request().accept(JSON).get();
        return getResponse(res, DeviceQuota.class);

    }
}
