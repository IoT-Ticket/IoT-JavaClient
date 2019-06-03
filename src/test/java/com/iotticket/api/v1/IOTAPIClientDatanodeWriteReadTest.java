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
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimeZone;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.iotticket.api.v1.model.DataType;
import com.iotticket.api.v1.model.DatanodeQueryCriteria;
import com.iotticket.api.v1.model.ProcessValues;
import com.iotticket.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.api.v1.model.Datanode.DatanodeReadValue;
import com.iotticket.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.api.v1.model.WriteDataResponse;
import com.iotticket.api.v1.util.ResourceFileUtils;

public class IOTAPIClientDatanodeWriteReadTest {
	
	private static final int WIREMOCK_PORT = 9999; 
    private static final String TEST_BASE_URL = "http://localhost:" + String.valueOf(WIREMOCK_PORT) + "/";
	
    private static final String TEST_DATA_NODE_WRITE_RESOURCE = "/process/write/153ffceb982745e8b1e8abacf9c217f3/";
    private static final String TEST_DATA_NODE_READ_RESOURCE_BASE = "/process/read/153ffceb982745e8b1e8abacf9c217f3/?datanodes=Engine/isRunning,latitude,longitude&limit=3&fromdate=1546293600000&todate=1546380000000";
    
    private static final String TEST_USERNAME = "user1";
	private static final String TEST_PASSWORD = "pw1";
	
	private static final String TEST_DEVICE_ID = "153ffceb982745e8b1e8abacf9c217f3";
	private static final String TEST_DATA_NAME = "Temperature";
	private static final Double TEST_DATA_VALUE = 5.5;
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(WIREMOCK_PORT);
	

	@Test
	public void testWriteDatanodes_oneDatanode() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		DatanodeWriteValue datanodeWriteValue = new DatanodeWriteValue();
		datanodeWriteValue.setName(TEST_DATA_NAME);
		datanodeWriteValue.setValue(TEST_DATA_VALUE);
		
		stubFor(post(
				urlEqualTo(TEST_DATA_NODE_WRITE_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString("testWriteDatanodes_oneDatanodeRequestBody.json", getClass())))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString("testWriteDatanodes_oneDatanodeResponseBody.json", getClass())))
				);
		
		WriteDataResponse result = iotApiClient.writeData(TEST_DEVICE_ID, datanodeWriteValue);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DATA_NODE_WRITE_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString("testWriteDatanodes_oneDatanodeRequestBody.json", getClass())))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(1, result.getTotalWritten());
		assertEquals(1, result.getWriteResults().size());
		assertEquals(new URI("https://my.iot-ticket.com/api/v1/process/read/153ffceb982745e8b1e8abacf9c217f3/?datanodes=/Temperature"), result.getWriteResults().get(0).getHref());
		assertEquals(1, result.getWriteResults().get(0).getWrittenCount());
	}
	
	@Test
	public void testWriteDatanodes_multipleDatanodes() throws Exception {
		
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);
		
		DatanodeWriteValue doubleData = new DatanodeWriteValue();
		doubleData.setName("doubleData");
		doubleData.setValue(1.1);
		
		DatanodeWriteValue longData = new DatanodeWriteValue();
		longData.setName("longData");
		longData.setValue(1);
		
		// String type is not parsed from value
		DatanodeWriteValue stringData = new DatanodeWriteValue();
		stringData.setName("stringData");
		stringData.setValue("true");
		stringData.setDataType(DataType.StringType);
		
		DatanodeWriteValue binaryData = new DatanodeWriteValue();
		binaryData.setName("binaryData");
		byte[] bytes = {0,1,0};
		binaryData.setValue(bytes);
		
		DatanodeWriteValue booleanData = new DatanodeWriteValue();
		booleanData.setName("booleanData");
		booleanData.setValue(true);
		
		Collection<DatanodeWriteValue> datanodes = new ArrayList<DatanodeWriteValue>();
		datanodes.add(doubleData);
		datanodes.add(longData);
		datanodes.add(stringData);
		datanodes.add(binaryData);
		datanodes.add(booleanData);

		stubFor(post(
				urlEqualTo(TEST_DATA_NODE_WRITE_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString("testWriteDatanodes_multipleDatanodesRequestBody.json", getClass())))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString("testWriteDatanodes_multipleDatanodesResponseBody.json", getClass())))
				);
		
		WriteDataResponse result = iotApiClient.writeData(TEST_DEVICE_ID, datanodes);
		
		verify(postRequestedFor(
				urlEqualTo(TEST_DATA_NODE_WRITE_RESOURCE))
				.withHeader("Accept", equalTo("application/json"))
				.withRequestBody(equalToJson(
						ResourceFileUtils.resourceFileToString("testWriteDatanodes_multipleDatanodesRequestBody.json", getClass())))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(5, result.getTotalWritten());
		assertEquals(5, result.getWriteResults().size());
		
	}

	@Test
	public void testReadDatanodes() throws Exception {
		IOTAPIClient iotApiClient = new IOTAPIClient(TEST_BASE_URL, TEST_USERNAME, TEST_PASSWORD);

		String datapoint1 = "latitude";
		String datapoint2 = "longitude";
		String datapoint3 = "Engine/isRunning";


		DatanodeQueryCriteria datanodeQueryCriteria = new DatanodeQueryCriteria(TEST_DEVICE_ID, datapoint1, datapoint2, datapoint3);
		
		Date fromDate = new Date(1546293600000L);
		Date toDate = new Date(1546380000000L);
		
		datanodeQueryCriteria.setFromDate(fromDate);
		datanodeQueryCriteria.setToDate(toDate);
		
		datanodeQueryCriteria.setLimit(3);
		
		stubFor(get(
				urlEqualTo(TEST_DATA_NODE_READ_RESOURCE_BASE))
				.withHeader("Accept", equalTo("application/json"))
				.withBasicAuth(TEST_USERNAME, TEST_PASSWORD)
				.willReturn(
						aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/json").
						withBody(ResourceFileUtils.resourceFileToString("testReadDatanodesResponseBody.json", getClass())))
				);
		
		ProcessValues result = iotApiClient.readProcessData(datanodeQueryCriteria);
		
		verify(getRequestedFor(
				urlEqualTo(TEST_DATA_NODE_READ_RESOURCE_BASE))
				.withHeader("Accept", equalTo("application/json"))
				.withBasicAuth(new BasicCredentials(TEST_USERNAME, TEST_PASSWORD)));
		
		assertEquals(result.getUri().toString(), "https://my.iot-ticket.com/api/v1/process/read/153ffceb982745e8b1e8abacf9c217f3?todate=1546380000000&limit=3&datanodes=latitude%2Clongitude%2C%2FEngine%2FisRunning&fromdate=1546293600000");
		assertEquals(3, result.getDatanodeReads().size());
		
		ArrayList<DatanodeRead> datanodeReads = new ArrayList<DatanodeRead>(result.getDatanodeReads());
		
		DatanodeRead datanodeRead1 = datanodeReads.get(0);
		assertEquals("longitude", datanodeRead1.getName());
		assertEquals(DataType.DoubleType, datanodeRead1.getDataType());
		assertEquals(1, datanodeRead1.getDatanodeReadValues().size());
		
		ArrayList<DatanodeReadValue> datanodeReadValues1 = new ArrayList<DatanodeReadValue>(datanodeRead1.getDatanodeReadValues());
		assertEquals("21.61577", datanodeReadValues1.get(0).getValue());
		assertEquals(1546293601000L, datanodeReadValues1.get(0).getTimestampMilliSeconds());
		
		DatanodeRead datanodeRead2 = datanodeReads.get(1);
		assertEquals("Engine/isRunning", datanodeRead2.getName());
		assertEquals(DataType.BooleanType, datanodeRead2.getDataType());
		assertEquals(1, datanodeRead2.getDatanodeReadValues().size());
		
		ArrayList<DatanodeReadValue> datanodeReadValues2 = new ArrayList<DatanodeReadValue>(datanodeRead2.getDatanodeReadValues());
		assertEquals("true", datanodeReadValues2.get(0).getValue());
		assertEquals(1546293601000L, datanodeReadValues2.get(0).getTimestampMilliSeconds());
	
		DatanodeRead datanodeRead3 = datanodeReads.get(2);
		assertEquals("latitude", datanodeRead3.getName());
		assertEquals(DataType.DoubleType, datanodeRead3.getDataType());
		assertEquals(1, datanodeRead3.getDatanodeReadValues().size());
		
		ArrayList<DatanodeReadValue> datanodeReadValues3 = new ArrayList<DatanodeReadValue>(datanodeRead3.getDatanodeReadValues());
		assertEquals("63.096", datanodeReadValues3.get(0).getValue());
		assertEquals(1546293601000L, datanodeReadValues3.get(0).getTimestampMilliSeconds());
		
	}
	
}
