package com.iotticket.api.v1.integrationTests;


import com.iotticket.api.v1.IOTAPIClient;

public abstract class IntegrationTestBase {

    static final String SERVER_URL = "https://my.iot-ticket.com/api/v1";
    static final String deviceId = "<device-id>";
    static final String userName = "<your-username>";
    static final String password = "<your-password";
    static final IOTAPIClient apiClient = new IOTAPIClient(SERVER_URL, userName, password);

}