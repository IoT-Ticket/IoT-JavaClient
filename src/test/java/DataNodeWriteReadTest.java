import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.DataType;
import com.iotticket.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.api.v1.model.Datanode.DatanodeReadValue;
import com.iotticket.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.api.v1.model.DatanodeQueryCriteria;
import com.iotticket.api.v1.model.DatanodeQueryCriteria.Order;
import com.iotticket.api.v1.model.Device.DeviceDetails;
import com.iotticket.api.v1.model.ProcessValues;
import com.iotticket.api.v1.model.WriteDataResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class DataNodeWriteReadTest extends TestBase {

    private final static byte[] testByteValue = new byte[]{1, 2, 3, 4, 5, 6, 7, 9, 10};
    public static String firstPath = "Engine/Auxillary";
    public static String secondPath = "Engine/Main";
    public static String numericDatanodeName = "AirVolume";
    private static boolean testBooleanValue = false;
    private static String boolDatanodeName = "LightOn";
    private String byteDatanodeName = "MP3";

    @Before
    public void setup() {
        boolDatanodeName = "LightOn";
        byteDatanodeName = "CANMessage";
        numericDatanodeName = "AirFlow";
        firstPath = "Engine/Auxillary";
        secondPath = "Engine/Main";
    }


    @Test
    public void writeBooleanValue() throws ValidAPIParamException {

        DatanodeWriteValue bv = new DatanodeWriteValue();
        bv.setName(boolDatanodeName);
        bv.setValue(testBooleanValue);

        WriteDataResponse writeResult = apiClient.writeData(deviceId, bv);
        assertNotNull(writeResult);
        assertTrue(writeResult.getTotalWritten() > 0);
        assertNotNull(writeResult.getWriteResults());
        assertTrue(writeResult.getWriteResults().iterator().next().getWrittenCount() > 0);
        assertNotNull(writeResult.getWriteResults().iterator().next().getHref());

        readBooleanValue();

    }


    public void readBooleanValue() {

        DatanodeQueryCriteria criteria = new DatanodeQueryCriteria(deviceId, boolDatanodeName);
        criteria.setDeviceId(deviceId);
        ProcessValues processData = apiClient.readProcessData(criteria);
        assertNotNull(processData.getUri());
        assertTrue(processData.getDatanodeReads().size() > 0);
        assertTrue(processData.getDatanodeReads().iterator().next().getDatanodeReadValues().size() > 0);
        assertEquals(DataType.BooleanType, processData.getDatanodeReads().iterator().next().getDataType());
        assertEquals(testBooleanValue, processData.getDatanodeReads().iterator().next().getDatanodeReadValues().iterator().next().getConvertedValue());

    }


    @Test
    public void writeBinaryValue() throws ValidAPIParamException {


        DatanodeWriteValue writeValue = new DatanodeWriteValue();
        writeValue.setName(byteDatanodeName);
        writeValue.setValue(testByteValue);


        WriteDataResponse writeResult = apiClient.writeData(deviceId, writeValue);
        assertNotNull(writeResult);
        assertTrue(writeResult.getTotalWritten() > 0);
        assertNotNull(writeResult.getWriteResults());
        assertTrue(writeResult.getWriteResults().iterator().next().getWrittenCount() > 0);
        assertNotNull(writeResult.getWriteResults().iterator().next().getHref());

        readBinaryValue();

    }


    private void readBinaryValue() {

        DatanodeQueryCriteria criteria = new DatanodeQueryCriteria(deviceId, byteDatanodeName);
        criteria.setDeviceId(deviceId);
        ProcessValues processData = apiClient.readProcessData(criteria);
        assertNotNull(processData.getUri());
        assertTrue(processData.getDatanodeReads().size() > 0);
        assertTrue(processData.getDatanodeReads().iterator().next().getDatanodeReadValues().size() > 0);

        assertEquals(DataType.BinaryType, processData.getDatanodeReads().iterator().next().getDataType());
        assertArrayEquals(testByteValue, (byte[]) processData.getDatanodeReads().iterator().next().getDatanodeReadValues().iterator().next().getConvertedValue());

    }


    @Test
    public void writeNumericValue() throws IoTServerCommunicationException, ValidAPIParamException {

        DeviceDetails device = apiClient.getDevice(deviceId);


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.SECOND, -1);
        Random r = new Random();

        Collection<DatanodeWriteValue> valuesToWrite = new ArrayList<DatanodeWriteValue>();
        for (int i = 0; i < 30; i++) {
            cal.add(Calendar.MILLISECOND, 1);

            DatanodeWriteValue dnwrite = new DatanodeWriteValue();
            dnwrite.setName(numericDatanodeName);
            dnwrite.setPath(firstPath);
            dnwrite.setUnit("l/s");
            dnwrite.setValue(r.nextInt(10));
            dnwrite.setTimestampMiliseconds(cal.getTimeInMillis());

            valuesToWrite.add(dnwrite);


            dnwrite = new DatanodeWriteValue();
            dnwrite.setName(numericDatanodeName);
            dnwrite.setPath(secondPath);
            dnwrite.setUnit("l/s");
            dnwrite.setValue(10 * r.nextDouble());
            dnwrite.setTimestampMiliseconds(cal.getTimeInMillis());

            valuesToWrite.add(dnwrite);

        }

        WriteDataResponse writeResult = apiClient.writeData(device.getDeviceId(), valuesToWrite);
        assertNotNull(writeResult);
        assertEquals(60, writeResult.getTotalWritten());
        assertNotNull(writeResult.getWriteResults());
        assertTrue(writeResult.getWriteResults().iterator().next().getWrittenCount() > 0);
        assertNotNull(writeResult.getWriteResults().iterator().next().getHref());


        readNumericValue();

    }

    private void readNumericValue() {
        readFirstNumericValue();
        readSecondNumericValues();

        /**Two datanodes are expected, since there are two datanodes with the name <numericDatanodeName>
         * but different paths
        */
        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, numericDatanodeName);
        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection<DatanodeRead> datanodeReads = processValues.getDatanodeReads();
        assertTrue(datanodeReads.size() == 2);


    }

    private void readSecondNumericValues() {



        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, secondPath + "/" + numericDatanodeName);
        crit.setSortOrder(Order.Descending);


        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection<DatanodeRead> datanodeReads = processValues.getDatanodeReads();
        for (DatanodeRead datanodeRead : datanodeReads) {

            Collection<DatanodeReadValue> values = datanodeRead.getDatanodeReadValues();
            assertEquals(DataType.DoubleType, datanodeRead.getDataType());
            assertTrue(values.size() == 1);

            for (DatanodeReadValue value : values) {
                long ts = value.getTimestampMilliSecond();
                double val = (Double) value.getConvertedValue();

                assertTrue(ts > 0);
                assertTrue(val < 10);

            }
        }
    }

    private void readFirstNumericValue() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.SECOND, -5);

        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, firstPath + "/" + numericDatanodeName);
        crit.setFromDate(cal.getTimeInMillis());
        crit.setLimit(100);
        crit.setSortOrder(Order.Ascending);

        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection<DatanodeRead> datanodeReads = processValues.getDatanodeReads();
        for (DatanodeRead datanodeRead : datanodeReads) {

            Collection<DatanodeReadValue> values = datanodeRead.getDatanodeReadValues();
            assertEquals(DataType.LongType, datanodeRead.getDataType());
            assertTrue(values.size() >= 30);

            for (DatanodeReadValue value : values) {
                long ts = value.getTimestampMilliSecond();
                long val = (Long) value.getConvertedValue();

                assertTrue(val < 10);
                assertTrue(ts > 0);

            }
        }
    }


}
