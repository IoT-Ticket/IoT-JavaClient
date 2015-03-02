import com.iotticket.api.v1.IOTAPIClient;

public abstract class TestBase {


//    final static String SERVER_URL = "https://my.iot-ticket.com/api/v1";
//    final static String deviceId = "<device-id>";
//    final static String userName = "<your-username>";
//    final static String password = "<your-password";
//    final static IOTAPIClient apiClient = new IOTAPIClient(SERVER_URL, userName,password);


    //    final static String SERVER_URL = "http://localhost:8080/api/v1";
    final static String SERVER_URL = "https://dev.wrm247.com/api/v1";
    //    final static String deviceId = "f1acb976a0aa4e889e8a01626d54d3bf";
    final static String deviceId = "8db27e19e9f14caea1306714d55a54b5";
    final static IOTAPIClient apiClient = new IOTAPIClient(SERVER_URL, "lawal", "w");


}
