package com.iotticket.api.v1;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.model.DataType;
import com.iotticket.api.v1.model.StatisticalDataQueryCriteria;
import com.iotticket.api.v1.model.StatisticalDataQueryCriteria.Grouping;
import com.iotticket.api.v1.model.StatisticalDataValues;
import com.iotticket.api.v1.model.StatisticalDataValues.StatisticalDataRead;
import com.iotticket.api.v1.model.StatisticalDataValues.StatisticalDataReadValue;

import com.iotticket.api.v1.util.ResourceFileUtils;

public class IOTAPIClientStatisticalDataTest {

	private static final int WIREMOCK_PORT = 9999; 
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
    private static final String RESOURCE_FILE_LOCATION = "com/iotticket/api/v1/";
    
    private static final String TEST_STATISTICAL_DATA_READ_RESOURCE = "/stat/read/153ffceb982745e8b1e8abacf9c217f3?todate=1546293802000&datanodes=latitude&fromdate=1546293600000";
    
    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);

	@Test
	public void testReadStatisticalData() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		StatisticalDataQueryCriteria criteria = new StatisticalDataQueryCriteria(TEST_DEVICE_ID, Grouping.Minute, 1546293600000L, 1546293802000L, "latitude");
		
		stubFor(get(
				urlEqualTo(TEST_STATISTICAL_DATA_READ_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testReadStatisticalDataResponseBody.json")))
				);
		
		StatisticalDataValues result = iotApiClient.readStatisticalData(criteria);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_STATISTICAL_DATA_READ_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
 
		assertEquals("https://my.iot-ticket.com/api/v1/stat/read/153ffceb982745e8b1e8abacf9c217f3?todate=1546293802000&datanodes=latitude&fromdate=1546293600000", result.getUri().toString());
		assertEquals(1, result.getStatisticalDataRead().size());
		
		ArrayList<StatisticalDataRead> statisticalDataReads = new ArrayList<StatisticalDataRead>(result.getStatisticalDataRead());
		StatisticalDataRead statisticalDataRead = statisticalDataReads.get(0);
		
		assertEquals(DataType.DoubleType, statisticalDataRead.getDataType());
		assertEquals("bar", statisticalDataRead.getUnit());
		assertEquals("Pressure", statisticalDataRead.getName());
		
		ArrayList<StatisticalDataReadValue> statisticalDataValues = new ArrayList<StatisticalDataReadValue>(statisticalDataRead.getStatisticalDataReadValues()); 
		
		StatisticalDataReadValue value1 = statisticalDataValues.get(0);
		assertEquals(0, value1.getCount());
		assertEquals("0", value1.getSum());
		assertEquals(1483228800000L, value1.getTimestampMilliSeconds());
		
		StatisticalDataReadValue value2 = statisticalDataValues.get(1);
		
		assertEquals("34.5", value2.getMinimum());
		assertEquals("65.7", value2.getMaximum());
		assertEquals("45.0", value2.getAverage());
		assertEquals(6546, value2.getCount());
		assertEquals("7674.6", value2.getSum());
		assertEquals(1514764800000L, value2.getTimestampMilliSeconds());
		
		StatisticalDataReadValue value3 = statisticalDataValues.get(2);
		
		assertEquals("32.5", value3.getMinimum());
		assertEquals("67.8", value3.getMaximum());
		assertEquals("46.4", value3.getAverage());
		assertEquals(3423, value3.getCount());
		assertEquals("7564.2", value3.getSum());
		assertEquals(1546300800000L, value3.getTimestampMilliSeconds());
		
	}
	
}
