package com.iotticket.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.Device.DeviceDetails;


public class IOTAPIClientDeviceTest{
	
	
    private static final int WIREMOCK_PORT = 9999;
    private static final String PATH_TO_REQUEST_AND_RESPONSE_BODIES = "com/iotticket/api/v1/";
    
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
	private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_NAME = "Test device";
	private static final String TEST_DEVICE_MANUFACTURER = "Test Oy";
	
	private static final String TEST_INVALID_CREDENTIALS_DESCRIPTION = "Provide a valid authorization credential";

	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";
	private final static String TEST_DEVICES_RESOURCE = "/devices/";
	    	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);
	
	@Test
	public void testRegisterDevice() throws ValidAPIParamException, IOException, URISyntaxException{
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER); 
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						readFromFile(PATH_TO_REQUEST_AND_RESPONSE_BODIES + "testRegisterDeviceRequestBody.json")))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(readFromFile(PATH_TO_REQUEST_AND_RESPONSE_BODIES + "testRegisterDeviceResponseBody.json")))
				);
		
		DeviceDetails result = iotApiClient.registerDevice(device);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						readFromFile(PATH_TO_REQUEST_AND_RESPONSE_BODIES + "testRegisterDeviceRequestBody.json")))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(result.getName(), TEST_DEVICE_NAME);
		assertEquals(result.getManufacturer(), TEST_DEVICE_MANUFACTURER);
		assertEquals(result.getDeviceId(), TEST_DEVICE_ID);
		
	}
	
	@Test(expected = IoTServerCommunicationException.class)
	public void testRegisterDevice_invalidCredentials() throws ValidAPIParamException, IOException, URISyntaxException{
		
		String wrongPassword = "pw2";
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, wrongPassword);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER); 
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						readFromFile(PATH_TO_REQUEST_AND_RESPONSE_BODIES + "testRegisterDeviceRequestBody.json")))
				.withBasicAuth(TEST_USERNAME, wrongPassword)
				.willReturn(
						aResponse()
						.withStatus(401)
						.withBody(readFromFile(PATH_TO_REQUEST_AND_RESPONSE_BODIES + "testRegisterDevice_invalidCredentialsResponseBody.json")))
				);
		
		iotApiClient.registerDevice(device);
	}
	
	// TODO: This method is needed in every unit test class so make this static and move to own class
	private String readFromFile(String filename) throws IOException, URISyntaxException {		
		Path path = Paths.get(getClass().getClassLoader()
			      .getResource(filename).toURI());
		
		byte[] fileAsBytes = Files.readAllBytes(path);
		return new String(fileAsBytes, StandardCharsets.UTF_8.name());
	}
	
}
