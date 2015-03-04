import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.Device.DeviceDetails;
import com.iotticket.api.v1.model.DeviceAttribute;
import com.iotticket.api.v1.model.ErrorInfo;
import com.iotticket.api.v1.model.PagedResult;
import org.junit.Test;

import static org.junit.Assert.*;


public class DeviceTest extends TestBase {


    public static final String DEVICENAME = "DreamCar";


    @Test
    public void newDeviceTest() {
        try {
            String deviceId = testDeviceRegister();
            fetchDevices(deviceId);
        } catch (IoTServerCommunicationException e) {
            ErrorInfo errorInfo = e.getErrorInfo();
            System.err.println(errorInfo);
            e.printStackTrace();
        } catch (ValidAPIParamException e) {
            e.printStackTrace();
        }
    }


    private String testDeviceRegister() throws IoTServerCommunicationException, ValidAPIParamException {
        final String type = "4WD";
        final String manufacturer = "NextGen Car Company";

        Device d = new Device();
        d.setName(DEVICENAME);
        d.setManufacturer(manufacturer);
        d.setType(type);
        d.getAttributes().add(new DeviceAttribute("EngineSize", "2.4"));
        d.getAttributes().add(new DeviceAttribute("FuelType", "Benzene"));


        DeviceDetails device = apiClient.registerDevice(d);

        assertNotNull(device);
        assertNotNull(device.getName());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getManufacturer());
        assertNotNull(device.getDeviceId());
        assertEquals(type, device.getType());
        assertEquals(DEVICENAME, device.getName());
        assertEquals(2, device.getAttributes().size());


        return device.getDeviceId();

    }


    private void fetchDevices(String newDeviceId) throws IoTServerCommunicationException {


        PagedResult<DeviceDetails> deviceList = apiClient.getDeviceList(0, 20);
        assertTrue(deviceList.getTotalCount() >= 1);
        assertEquals(0, deviceList.getSkip());
        assertEquals(20, deviceList.getRequestedCount());

        boolean found = false;
        for (DeviceDetails device : deviceList.getResults()) {
            if (device.getDeviceId().equals(newDeviceId)) {
                assertEquals(DEVICENAME, device.getName());
                found = true;
                break;
            }
        }
        assertTrue(found);

    }


    @Test
    public void fetchDevice() throws IoTServerCommunicationException {

        DeviceDetails device = apiClient.getDevice(deviceId);

        assertNotNull(device);
        assertNotNull(device.getName());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getManufacturer());
        assertEquals(device.getDeviceId(), deviceId);
        assertEquals(device.getUri().toString(), SERVER_URL + "/devices/" + deviceId);


    }


}
