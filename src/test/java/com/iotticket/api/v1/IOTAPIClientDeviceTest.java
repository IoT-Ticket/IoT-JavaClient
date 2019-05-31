package com.iotticket.api.v1;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.DeviceAttribute;
import com.iotticket.api.v1.model.Device.DeviceDetails;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;


import static com.github.tomakehurst.wiremock.client.WireMock.*;


public class IOTAPIClientDeviceTest{
	
    private static final int TEST_PORT = 9999;
	private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(TEST_PORT) + "/";
	private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_NAME = "Test device";
	private static final String TEST_DEVICE_MANUFACTURER = "Test Oy";
	
	private static final String TEST_INVALID_CREDENTIALS_DESCRIPTION = "Provide a valid authorization credential";

	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";
	private final static String TEST_DEVICES_RESOURCE = "/devices/";
	    	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(TEST_PORT);
	
	@Test
	public void testRegisterDevice() throws ValidAPIParamException{
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER); 
		
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						"{" + 
						"	\"name\": \"Test device\"," + 
						"	\"manufacturer\": \"Test Oy\"," +
						"	\"attributes\": []" +
						"}"))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(
								"{" + 
								"	\"attributes\": []," + 
								"	\"createdAt\": \"2014-12-03T10:31:05UTC\"," + 
								"	\"deviceId\": \"153ffceb982745e8b1e8abacf9c217f3\"," + 
								"	\"href\": \"https://www.test-url.com:9999/devices/153ffceb982745e8b1e8abacf9c217f3\"," + 
								"	\"name\": \"Test device\"," + 
								"	\"manufacturer\": \"Test Oy\"," + 
								"	\"resourceId\": \"X123\"" + 
								"}"))
				);
		
		DeviceDetails result = iotApiClient.registerDevice(device);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						"{" + 
						"	\"name\": \"Test device\"," + 
						"	\"manufacturer\": \"Test Oy\"," +
						"	\"attributes\": []" +
						"}"))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(result.getName(), TEST_DEVICE_NAME);
		assertEquals(result.getManufacturer(), TEST_DEVICE_MANUFACTURER);
		assertEquals(result.getDeviceId(), TEST_DEVICE_ID);
		
	}
	
	@Test(expected = IoTServerCommunicationException.class)
	public void testRegisterDevice_invalidCredentials() throws ValidAPIParamException{
		
		String wrongPassword = "pw2";
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, wrongPassword);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER); 
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						"{" + 
						"	\"name\": \"Test device\"," + 
						"	\"manufacturer\": \"Test Oy\"," +
						"	\"attributes\": []" +
						"}"))
				.withBasicAuth(TEST_USERNAME, wrongPassword)
				.willReturn(
						aResponse()
						.withStatus(401)
						.withBody(
								"{" + 
								"  \"description\": \"Provide a valid authorization credential\"," + 
								"  \"code\": 8001," + 
								"  \"moreInfo\": \"https://my.iot-ticket.com/api/v1/errorcodes\"," + 
								"  \"apiver\": 1" + 
								"}"))
				);
		
		DeviceDetails result = iotApiClient.registerDevice(device);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						"{" + 
						"	\"name\": \"Test device\"," + 
						"	\"manufacturer\": \"Test Oy\"," +
						"	\"attributes\": []" +
						"}"))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, wrongPassword)));
		
		assertEquals(result.getDescription(), TEST_INVALID_CREDENTIALS_DESCRIPTION);
		
	}
	
}
