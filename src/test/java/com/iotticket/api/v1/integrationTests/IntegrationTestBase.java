package com.iotticket.api.v1.integrationTests;


import com.iotticket.api.v1.IOTAPIClient;

public abstract class IntegrationTestBase {


    final static String SERVER_URL = "https://my.iot-ticket.com/api/v1";
    final static String deviceId = "<device-id>";
    final static String userName = "<your-username>";
    final static String password = "<your-password";
    final static IOTAPIClient apiClient = new IOTAPIClient(SERVER_URL, userName,password);

}