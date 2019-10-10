package com.iotticket.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.Device.DeviceDetails;
import com.iotticket.api.v1.model.DeviceAttribute;
import com.iotticket.api.v1.model.ErrorInfo;
import com.iotticket.api.v1.model.PagedResult;
import com.iotticket.api.v1.util.ResourceFileUtils;


public class IOTAPIClientDeviceTest{
	
	
    private static final int WIREMOCK_PORT = 9999; 
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
    private static final String RESOURCE_FILE_LOCATION = "com/iotticket/api/v1/";
    
    private static final String TEST_DEVICES_RESOURCE = "/devices/";
    
    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_NAME = "Test device";
	private static final String TEST_DEVICE_MANUFACTURER = "Test Oy";
	private static final String TEST_DEVICE_TYPE = "PC";
	private static final String TEST_ENTERPRISE_ID = "E123";
	private static final String TEST_ENTERPRISE_NAME = "Test Enterprise";
	private static final String TEST_DEVICE_DESCRIPTION = "The main server";
	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";
	    
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(WIREMOCK_PORT).notifier(new ConsoleNotifier(false)));
	
	
	@Test
	public void testRegisterDevice_withRequiredPropertiesOnly() throws ValidAPIParamException, IOException, URISyntaxException{
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER);
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withRequiredPropertiesOnlyRequestBody.json")))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withRequiredPropertiesOnlyResponseBody.json")))
				);
		
		DeviceDetails result = iotApiClient.registerDevice(device);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withRequiredPropertiesOnlyRequestBody.json")))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(TEST_DEVICE_NAME, result.getName());
		assertEquals(TEST_DEVICE_MANUFACTURER, result.getManufacturer());
		assertEquals(TEST_DEVICE_ID, result.getDeviceId());
		
		// If enterprise id is not defined in request body, user's root enterprise is returned here according to documentation.
		assertEquals(TEST_ENTERPRISE_ID, result.getEnterpriseId());
		assertEquals(TEST_ENTERPRISE_NAME, result.getEnterpriseName());
		assertNull(result.getType());
		assertNull(result.getDescription());
		assertTrue(result.getAttributes().isEmpty());
		
	}
	
	@Test
	public void testRegisterDevice_withOptionalProperties() throws ValidAPIParamException, IOException, URISyntaxException{
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		DeviceAttribute attribute1 = new DeviceAttribute("Application Version", "0.2.3");
		DeviceAttribute attribute2 = new DeviceAttribute("Chip", "TestCore");
		
		Collection<DeviceAttribute> deviceAttributes = new ArrayList<DeviceAttribute>();
		deviceAttributes.add(attribute1);
		deviceAttributes.add(attribute2);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER);
		device.setType(TEST_DEVICE_TYPE);
		device.setDescription(TEST_DEVICE_DESCRIPTION);
		device.setEnterpriseId(TEST_ENTERPRISE_ID);
		device.setAttributes(deviceAttributes);
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withOptionalPropertiesRequestBody.json")))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withOptionalPropertiesResponseBody.json")))
				);
		
		DeviceDetails result = iotApiClient.registerDevice(device);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_withOptionalPropertiesRequestBody.json")))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));

		assertEquals(TEST_DEVICE_NAME, result.getName());
		assertEquals(TEST_DEVICE_MANUFACTURER, result.getManufacturer());
		assertEquals(TEST_DEVICE_ID, result.getDeviceId());
		assertEquals(TEST_ENTERPRISE_ID, result.getEnterpriseId());
		assertEquals(TEST_ENTERPRISE_NAME, result.getEnterpriseName());
		assertEquals(TEST_DEVICE_TYPE, result.getType());
		assertEquals(TEST_DEVICE_DESCRIPTION, result.getDescription());
		
		assertFalse(result.getAttributes().isEmpty());
		assertEquals(2, result.getAttributes().size());
		
	}
	
	public void testRegisterDevice_invalidCredentials() throws ValidAPIParamException, IOException, URISyntaxException{
		
		String wrongPassword = "pw2";
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, wrongPassword);
		
		DeviceAttribute attribute1 = new DeviceAttribute("Application Version", "0.2.3");
		DeviceAttribute attribute2 = new DeviceAttribute("Chip", "TestCore");
		
		Collection<DeviceAttribute> deviceAttributes = new ArrayList<DeviceAttribute>();
		deviceAttributes.add(attribute1);
		deviceAttributes.add(attribute2);
		
		Device device = new Device();
		device.setName(TEST_DEVICE_NAME);
		device.setManufacturer(TEST_DEVICE_MANUFACTURER);
		device.setType(TEST_DEVICE_TYPE);
		device.setDescription(TEST_DEVICE_DESCRIPTION);
		device.setAttributes(deviceAttributes);
		
		stubFor(post(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDeviceRequestBody.json")))
				.withBasicAuth(TEST_USERNAME, wrongPassword)
				.willReturn(
						aResponse()
						.withStatus(401)
						.withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDevice_invalidCredentialsResponseBody.json")))
				);
		
		try {
			iotApiClient.registerDevice(device);
			fail("Expected IoTServerCommunicationException");
		} catch (IoTServerCommunicationException exception) {
			assertEquals("Request with server was unsuccesful, check the errorInfo object for further details", exception.getMessage());
			
			ErrorInfo errorInfo = exception.getErrorInfo();
			assertEquals("Provide a valid authorization credential", errorInfo.description);
			assertEquals(8001, errorInfo.code);
			assertEquals("https://my.iot-ticket.com/api/v1/errorcodes", errorInfo.moreInfo);
			assertEquals(1, errorInfo.apiver);
		}
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DEVICES_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testRegisterDeviceRequestBody.json")))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));	
	}


	@Test 
	public void testGetDevices() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		String getDevicesResource = TEST_DEVICES_RESOURCE + "?limit=2&offset=1";
		
		stubFor(get(
				urlEqualTo(getDevicesResource))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetDevicesResponseBody.json")))
				);
		
		PagedResult<DeviceDetails> result = iotApiClient.getDeviceList(1, 2);
		
		verify(getRequestedFor(
				urlEqualTo(getDevicesResource))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(17, result.getTotalCount());
		assertEquals(2, result.getRequestedCount());
		assertEquals(1, result.getSkip());
		assertEquals(2, result.getResults().size());
		
		ArrayList<DeviceDetails> deviceDetailsList = new ArrayList<DeviceDetails>(result.getResults());
		
		DeviceDetails deviceDetails1 = deviceDetailsList.get(0);
		
		assertEquals(2, deviceDetails1.getAttributes().size());
		assertEquals("153ffceb982745e8b1e8abacf9c217f3", deviceDetails1.getDeviceId());
		assertEquals("NextGen Car Company", deviceDetails1.getManufacturer());
		assertEquals("DreamCar", deviceDetails1.getName());
		assertEquals("4WD", deviceDetails1.getType());
		assertEquals(TEST_ENTERPRISE_ID, deviceDetails1.getEnterpriseId());
		assertEquals(TEST_ENTERPRISE_NAME, deviceDetails1.getEnterpriseName());
		assertEquals("https://my.iot-ticket.com/api/v1/devices/153ffceb982745e8b1e8abacf9c217f3", deviceDetails1.getUri().toString());
		
		DeviceDetails deviceDetails2 = deviceDetailsList.get(1);
		
		assertEquals(0, deviceDetails2.getAttributes().size());
		assertEquals("253ffceb982745e8b1e8abacf9c217f3", deviceDetails2.getDeviceId());
		assertEquals("Test Car Company", deviceDetails2.getManufacturer());
		assertEquals("Test car", deviceDetails2.getName());
		assertEquals("2WD", deviceDetails2.getType());
		assertEquals(TEST_ENTERPRISE_ID, deviceDetails2.getEnterpriseId());
		assertEquals(TEST_ENTERPRISE_NAME, deviceDetails2.getEnterpriseName());
		assertEquals("https://my.iot-ticket.com/api/v1/devices/253ffceb982745e8b1e8abacf9c217f3", deviceDetails2.getUri().toString());

	}
	
	@Test
	public void testGetDevices_noDevicesFound() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		String getDevicesResource = TEST_DEVICES_RESOURCE + "?limit=10&offset=0";
		
		stubFor(get(
				urlEqualTo(getDevicesResource))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetDevices_noDevicesFoundResponseBody.json")))
				);
		
		PagedResult<DeviceDetails> result = iotApiClient.getDeviceList(0, 10);
		
		verify(getRequestedFor(
				urlEqualTo(getDevicesResource))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(0, result.getTotalCount());
		assertEquals(10, result.getRequestedCount());
		assertEquals(0, result.getSkip());
		assertTrue(result.getResults().isEmpty());
	}

	@Test
	public void testGetDevice() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		String getDeviceResource = TEST_DEVICES_RESOURCE + TEST_DEVICE_ID + "/";
		
		stubFor(get(
				urlEqualTo(getDeviceResource))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json")
						.withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetDeviceResponseBody.json")))
				);
		
		DeviceDetails result = iotApiClient.getDevice(TEST_DEVICE_ID);
		
		verify(getRequestedFor(
				urlEqualTo(getDeviceResource))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(2, result.getAttributes().size());
		assertEquals("153ffceb982745e8b1e8abacf9c217f3", result.getDeviceId());
		assertEquals("NextGen Car Company", result.getManufacturer());
		assertEquals("DreamCar", result.getName());
		assertEquals(TEST_ENTERPRISE_ID, result.getEnterpriseId());
		assertEquals(TEST_ENTERPRISE_NAME, result.getEnterpriseName());
		assertEquals("4WD", result.getType());
		assertEquals("https://my.iot-ticket.com/api/v1/devices/153ffceb982745e8b1e8abacf9c217f3", result.getUri().toString());
	}
	
	
	@Test
	public void testGetDevice_deviceNotFound() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		String invalidDeviceId = "01234567892745e8b1e8abacf9c21123";
		String getDeviceResource = TEST_DEVICES_RESOURCE + invalidDeviceId + "/";
		
		stubFor(get(
				urlEqualTo(getDeviceResource))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(403)
						.withHeader("Content-Type", "application/json")
						.withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetDevice_deviceNotFoundResponseBody.json")))
				);
		
		try {
			iotApiClient.getDevice(invalidDeviceId);
			fail();
		} catch (IoTServerCommunicationException exception) {
			assertEquals("Request with server was unsuccesful, check the errorInfo object for further details", exception.getMessage());
			
			ErrorInfo errorInfo = exception.getErrorInfo();
			assertEquals("Re-check device id and ensure device access is valid", errorInfo.description);
			assertEquals(8001, errorInfo.code);
			assertEquals("https://my.iot-ticket.com/api/v1/errorcodes", errorInfo.moreInfo);
			assertEquals(1, errorInfo.apiver);
		}
	}
	
}
