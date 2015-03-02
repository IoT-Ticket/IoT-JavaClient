import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.model.DeviceQuota;
import com.iotticket.api.v1.model.Quota;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;

public class QuotaTest extends TestBase {

    @Test
    public void getClientQuota() throws IoTServerCommunicationException {

        Quota quota = apiClient.getQuota();
        assertNotNull(quota);
        assertNotNull(quota.getMaxDataNodePerDevice());
        assertNotNull(quota.getMaxNumberOfDevices());
        assertNotNull(quota.getMaxStorageSize());
        assertNotNull(quota.getTotalDevices());
        assertNotNull(quota.getUsedStorageSize());

    }

    @Test
    public void getDeviceQuota() throws IoTServerCommunicationException {

        DeviceQuota deviceQuota = apiClient.getDeviceQuota(deviceId);
        assertNotNull(deviceQuota);
        assertNotNull(deviceQuota.getDeviceId());
        assertEquals(deviceQuota.getDeviceId(), deviceId);
        assertNotNull(deviceQuota.getMaxReadRequestPerDay());
        assertNotNull(deviceQuota.getNumberOfDataNodes());
        assertNotNull(deviceQuota.getStorageSize());
        assertNotNull(deviceQuota.getTotalRequestToday());


    }

}
