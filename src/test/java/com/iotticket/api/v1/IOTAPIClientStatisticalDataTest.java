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
import java.util.HashSet;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
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
    
    private static final String TEST_STATISTICAL_DATA_READ_RESOURCE = "/stat/read/153ffceb982745e8b1e8abacf9c217f3/?datanodes=latitude&grouping=Minute&fromdate=1546293600000&todate=1546293802000";
    private static final String TEST_STATISTICAL_DATA_MULTIPLE_VTAGS_READ_RESOURCE = "/stat/read/153ffceb982745e8b1e8abacf9c217f3/?datanodes=latitude&grouping=Minute&fromdate=1546293600000&todate=1546293802000&vtags=vtag1,vtag2";
    
    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(options().port(WIREMOCK_PORT).notifier(new ConsoleNotifier(false)));

	@Test
	public void testReadStatisticalData_noVTags() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		StatisticalDataQueryCriteria criteria = new StatisticalDataQueryCriteria(TEST_DEVICE_ID, Grouping.Minute, 1546293600000L, 1546293802000L, "latitude");
		
		stubFor(get(
				urlEqualTo(TEST_STATISTICAL_DATA_READ_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testReadStatisticalData_noVTagsResponseBody.json")))
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
		assertEquals(0, value1.getCount().longValue());
		assertEquals(0, value1.getSum().doubleValue(), 0);
		assertEquals(1483228800000L, value1.getTimestampMilliSeconds().longValue());
		assertNull(value1.getMinimum());
		assertNull(value1.getMaximum());
		assertNull(value1.getAverage());
		
		StatisticalDataReadValue value2 = statisticalDataValues.get(1);
		
		assertEquals(34.5, value2.getMinimum().doubleValue(), 0);
		assertEquals(65.7, value2.getMaximum().doubleValue(), 0);
		assertEquals(45.0, value2.getAverage().doubleValue(), 0);
		assertEquals(6546L, value2.getCount().longValue());
		assertEquals(7674.6, value2.getSum().doubleValue(), 0);
		assertEquals(1514764800000L, value2.getTimestampMilliSeconds().longValue());
		
		StatisticalDataReadValue value3 = statisticalDataValues.get(2);
		
		assertEquals(32.5, value3.getMinimum().doubleValue(), 0);
		assertEquals(67.8, value3.getMaximum().doubleValue(), 0);
		assertEquals(46.4, value3.getAverage().doubleValue(), 0);
		assertEquals(3423L, value3.getCount().longValue());
		assertEquals(7564.2, value3.getSum(). doubleValue(), 0);
		assertEquals(1546300800000L, value3.getTimestampMilliSeconds().longValue());
		
	}
	
	@Test
	public void testReadStatisticalData_multipleVTags() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		StatisticalDataQueryCriteria criteria = new StatisticalDataQueryCriteria(TEST_DEVICE_ID, Grouping.Minute, 1546293600000L, 1546293802000L, "latitude");
		
		HashSet<String> vtags = new HashSet<String>();
		vtags.add("vtag1");
		vtags.add("vtag2");
		
		criteria.setVtags(vtags);
		
		stubFor(get(
				urlEqualTo(TEST_STATISTICAL_DATA_MULTIPLE_VTAGS_READ_RESOURCE))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString(RESOURCE_FILE_LOCATION + "testReadStatisticalData_multipleVTagsResponseBody.json")))
				);
		
		StatisticalDataValues result = iotApiClient.readStatisticalData(criteria);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_STATISTICAL_DATA_MULTIPLE_VTAGS_READ_RESOURCE))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
 
		assertEquals("https://my.iot-ticket.com/api/v1/stat/read/153ffceb982745e8b1e8abacf9c217f3?datanodes=latitude&grouping=Minute&fromdate=1546293600000&todate=1546293802000&vtags=vtag1,vtag2", result.getUri().toString());
		assertEquals(1, result.getStatisticalDataRead().size());
		
		ArrayList<StatisticalDataRead> statisticalDataReads = new ArrayList<StatisticalDataRead>(result.getStatisticalDataRead());
		StatisticalDataRead statisticalDataRead = statisticalDataReads.get(0);
		
		assertEquals(DataType.LongType, statisticalDataRead.getDataType());
		assertEquals("", statisticalDataRead.getUnit());
		assertEquals("number1", statisticalDataRead.getName());
		
		ArrayList<StatisticalDataReadValue> statisticalDataValues = new ArrayList<StatisticalDataReadValue>(statisticalDataRead.getStatisticalDataReadValues()); 
		assertEquals(1, statisticalDataValues.size());
		
		StatisticalDataReadValue value1 = statisticalDataValues.get(0);
		assertEquals(3, value1.getCount().longValue());
		assertEquals(10.0, value1.getSum().doubleValue(), 0);
		assertEquals(1546293600000L, value1.getTimestampMilliSeconds().longValue());
		assertEquals(2.0, value1.getMinimum(), 0.0);
		assertEquals(5.0, value1.getMaximum(), 0.0);
		assertEquals(3.5, value1.getAverage(), 0.0);
		
	}
	
}
