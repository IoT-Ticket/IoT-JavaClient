package com.iotticket.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.model.Enterprise;
import com.iotticket.api.v1.model.PagedResult;
import com.iotticket.api.v1.util.ResourceFileUtils;

public class IOTAPIClientEnterpriseTest {

    private static final int WIREMOCK_PORT = 9999; 
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
    private static final String RESOURCE_FILE_LOCATION = "com/iotticket/api/v1/";

    private static final String TEST_GET_ROOT_ENTERPRICES_RESOURCE = "/enterprises/?limit=10&offset=0";
    private static final String TEST_GET_SUB_ENTERPRICES_RESOURCE = "/enterprises/6015/?limit=10&offset=0";


    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";

	
    @Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(WIREMOCK_PORT).notifier(new ConsoleNotifier(false)));
	
    
	@Test
	public void testGetRootEnterprises() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_GET_ROOT_ENTERPRICES_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetEnterprisesResponseBody.json")))
				);
		
		PagedResult<Enterprise> result = iotApiClient.getRootEnterprises(10, 0);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_GET_ROOT_ENTERPRICES_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(15, result.getTotalCount());
		assertEquals(10, result.getRequestedCount());
		assertEquals(0, result.getSkip());
		assertEquals(2, result.getResults().size());

		ArrayList<Enterprise> enterprises = new ArrayList<Enterprise>(result.getResults());
		Enterprise enterprise1 = enterprises.get(0);
		
		assertEquals("https://my.iot-ticket.com/api/v1/enterprises/5678", enterprise1.getUri().toString());
		assertEquals("Enterprise 3", enterprise1.getName());
		assertEquals("E5678", enterprise1.getResourceId());
		assertEquals(true, enterprise1.getHasSubEnterprises());
		
		Enterprise enterprise2 = enterprises.get(1);
		
		assertEquals("https://my.iot-ticket.com/api/v1/enterprises/6789", enterprise2.getUri().toString());
		assertEquals("Enterprise 4", enterprise2.getName());
		assertEquals("E6789", enterprise2.getResourceId());
		assertEquals(false, enterprise2.getHasSubEnterprises());

	}

	
	@Test
	public void testGetRootEnterprises_noRootEnterprisesFound() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_GET_ROOT_ENTERPRICES_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetEnterprises_noEnterprisesFoundResponseBody.json")))
				);
		
		PagedResult<Enterprise> result = iotApiClient.getRootEnterprises(10, 0);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_GET_ROOT_ENTERPRICES_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(0, result.getTotalCount());
		assertEquals(10, result.getRequestedCount());
		assertEquals(0, result.getSkip());
		assertEquals(0, result.getResults().size());
	}
	
	
	@Test
	public void testGetSubEnterprises() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_GET_SUB_ENTERPRICES_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetEnterprisesResponseBody.json")))
				);
		
		PagedResult<Enterprise> result = iotApiClient.getSubEnterprises("6015", 10, 0);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_GET_SUB_ENTERPRICES_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(15, result.getTotalCount());
		assertEquals(10, result.getRequestedCount());
		assertEquals(0, result.getSkip());
		assertEquals(2, result.getResults().size());

		ArrayList<Enterprise> enterprises = new ArrayList<Enterprise>(result.getResults());
		Enterprise enterprise1 = enterprises.get(0);
		
		assertEquals("https://my.iot-ticket.com/api/v1/enterprises/5678", enterprise1.getUri().toString());
		assertEquals("Enterprise 3", enterprise1.getName());
		assertEquals("E5678", enterprise1.getResourceId());
		assertTrue(enterprise1.getHasSubEnterprises());
		
		Enterprise enterprise2 = enterprises.get(1);
		
		assertEquals("https://my.iot-ticket.com/api/v1/enterprises/6789", enterprise2.getUri().toString());
		assertEquals("Enterprise 4", enterprise2.getName());
		assertEquals("E6789", enterprise2.getResourceId());
		assertFalse(enterprise2.getHasSubEnterprises());
		
	}
	
	
	@Test
	public void testGetSubEnterprises_noSubEnterprisesFound() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		stubFor(get(
				urlEqualTo(TEST_GET_SUB_ENTERPRICES_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testGetEnterprises_noEnterprisesFoundResponseBody.json")))
				);
		
		PagedResult<Enterprise> result = iotApiClient.getSubEnterprises("6015", 10, 0);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_GET_SUB_ENTERPRICES_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(0, result.getTotalCount());
		assertEquals(10, result.getRequestedCount());
		assertEquals(0, result.getSkip());
		assertEquals(0, result.getResults().size());

	}
	
}
