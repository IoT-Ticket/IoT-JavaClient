package com.iotticket.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.model.DeviceQuota;
import com.iotticket.api.v1.model.Quota;
import com.iotticket.api.v1.util.ResourceFileUtils;

public class IOTAPIClientQuotaTest {

    private static final int WIREMOCK_PORT = 9999; 
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
    private static final String RESOURCE_FILE_LOCATION = "com/iotticket/api/v1/";
    
    private static final String TEST_OVERALL_QUOTA_RESOURCE = "/quota/all/";
    private static final String TEST_DEVICE_QUOTA_RESOURCE = "/quota/153ffceb982745e8b1e8abacf9c217f3/";
    
    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";
	
    @Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(WIREMOCK_PORT).notifier(new ConsoleNotifier(false)));
    
    @Test
    public void testGetQuota() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_OVERALL_QUOTA_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetQuotaResponseBody.json")))
				);
		
		Quota result = iotApiClient.getQuota();
		
		verify(getRequestedFor(
				urlEqualTo(TEST_OVERALL_QUOTA_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(new Integer(3), result.getTotalDevices());
		assertEquals(new Integer(5), result.getMaxNumberOfDevices());
		assertEquals(new Integer(10), result.getMaxDataNodePerDevice());
		assertEquals(new Long(1048576L), result.getUsedStorageSize());
		assertEquals(new Long(52428800L), result.getMaxStorageSize());
    	
    }
    
    @Test
    public void testGetDeviceQuota() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_DEVICE_QUOTA_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetDeviceQuotaResponseBody.json")))
				);
		
		DeviceQuota result = iotApiClient.getDeviceQuota(TEST_DEVICE_ID);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_DEVICE_QUOTA_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(TEST_DEVICE_ID, result.getDeviceId());
		assertEquals(new Long(5000L), result.getTotalRequestToday());
		assertEquals(new Integer(100000), result.getMaxReadRequestPerDay());
		assertEquals(new Integer(5), result.getNumberOfDataNodes());
		assertEquals(new Long(3072L), result.getStorageSize());
    }
}
