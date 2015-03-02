import com.iotticket.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.DeviceAttribute;
import com.iotticket.api.v1.validation.Validatable;
import com.iotticket.api.v1.validation.ValidationRunner;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class ValidationTest {


    private static ValidationRunner validator = new ValidationRunner();

    @Test
    public void testDataWriteValidation() {


        DatanodeWriteValue writeValue = new DatanodeWriteValue();
        failureExpected(writeValue, "No Name set");

        writeValue.setName(repeatNTimes("a", 101));
        writeValue.setValue(true);
        failureExpected(writeValue, "Datanode name should not exceed 100 char");


        writeValue.setValue(null);
        writeValue.setName("Pressure");
        failureExpected(writeValue, "Value not set yet");


        writeValue.setValue(64.3f);
        failureNotExpected(writeValue);
        assertEquals(writeValue.getValue(), "64.3");


        writeValue.setValue(true);
        assertEquals(writeValue.getValue(), "true");


        writeValue.setValue(Integer.MIN_VALUE);
        assertEquals(writeValue.getValue(), Integer.toString(Integer.MIN_VALUE));


        writeValue.setPath("1/2/3/4/5/6/7/8/9/10/11");
        failureExpected(writeValue, "Number of path exceeded ten");

        writeValue.setPath("/1/2/3/4/5/6/7/8/9/10");
        failureNotExpected(writeValue);

        writeValue.setPath("1/2/3/4/5/6/7/8/9/10");
        failureNotExpected(writeValue);

        writeValue.setUnit("WilliamThomsonK");
        failureExpected(writeValue, "Unit should not have more than 10 char");


        writeValue.setUnit("C");
        failureNotExpected(writeValue);


    }

    private void failureNotExpected(Validatable validatable) {
        try {
            validator.runValidation(validatable);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void failureExpected(Validatable validatable, String failureReason) {
        try {
            validator.runValidation(validatable);
            fail("It should fail because :" + failureReason);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private static String repeatNTimes(String str, int numberOfTimes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfTimes; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    @Test
    public void testDeviceValidation() {


        Device device = new Device();
        failureExpected(device, "Device has no name set");

        device.setName("device1");
        failureExpected(device, "Device has no manufacturer set");

        device.setManufacturer("ThisDeviceManufacturerHasChosenARatherLongNameThereforeTheAPIDemandsThatItsShortensItNameTo:IOTTICKET");
        failureExpected(device, "manufacturer attribute exceeds 100 character");

        device.setManufacturer("IOTTICKET");
        failureNotExpected(device);


        for (int i = 0; i < 51; i++) {
            device.getAttributes().add(new DeviceAttribute("attribute" + i, "attributevlue" + i));
        }

        failureExpected(device, "Number of Attributes exceeded. Should not be more than 50");


        DeviceAttribute attr = new DeviceAttribute(repeatNTimes("k", 256), "attrvalue");
        device.setAttributes(Collections.singleton(attr));
        failureExpected(device, "Attr Key Exceeds 255 char");

        attr = new DeviceAttribute("Attrkey", repeatNTimes("v", 256));
        device.setAttributes(Collections.singleton(attr));
        failureExpected(device, "Attr Value Exceeds 255 char");


    }


}
