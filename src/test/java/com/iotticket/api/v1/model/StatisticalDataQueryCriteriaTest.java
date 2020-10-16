package com.iotticket.api.v1.model;

import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class StatisticalDataQueryCriteriaTest {

    private static final String DATA_PATH_STRING_SEPARATOR = ",";

    @Test
    public void testGetDataPathsAsStringForOneDatapoint() {
        final String onlyDatapoint = "ThisIsATest";
        StatisticalDataQueryCriteria statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithDatapoints(onlyDatapoint);
        assertEquals(onlyDatapoint, statisticalDataQueryCriteria.getDataPathsAsString());
    }

    @Test
    public void testGetDataPathsAsStringForMultipleDatapoints() {
        final List<String> expectedList = Arrays.asList("first", "second", "third", "fourth", "fifth");
        final int expectedCount = expectedList.size();
        StatisticalDataQueryCriteria statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithDatapoints((String[]) expectedList.toArray());
        final String testString = statisticalDataQueryCriteria.getDataPathsAsString();
        // The order is not guaranteed, so split by the separator.
        String[] stringPartsArr = testString.split(DATA_PATH_STRING_SEPARATOR);
        assertEquals(expectedCount, stringPartsArr.length);
        // Add the parts to a set, and check if the set contains the expected data points
        HashSet<String> stringPartSet = new HashSet<>(expectedCount);
        stringPartSet.addAll(Arrays.asList(stringPartsArr));
        // Check if the stringPartSet contains all the expected strings.
        for (String expectedString : expectedList) {
            assertTrue(expectedString + " should have been in the dataPathsString.",
                       stringPartSet.contains(expectedString));
        }
    }

    @Test
    public void testGetFromDate() {
        // Test the Date constructor
        final Date fromDate = new Date(1000000L);
        StatisticalDataQueryCriteria statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithFromDate(fromDate);
        assertEquals(fromDate.getTime(), statisticalDataQueryCriteria.getFromDate().longValue());

        // Test the long constructor
        final long fromDateLong = 2000000L;
        statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithFromDate(fromDateLong);
        assertEquals(fromDateLong, statisticalDataQueryCriteria.getFromDate().longValue());
    }

    @Test
    public void testGetToDate() {
        // Test the Date constructor
        final Date toDate = new Date(1000000L);
        StatisticalDataQueryCriteria statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithToDate(toDate);
        assertEquals(toDate.getTime(), statisticalDataQueryCriteria.getToDate().longValue());

        // Test the long constructor
        final long toDateLong = 2000000L;
        statisticalDataQueryCriteria = getExampleStatisticalDataQueryCriteriaWithToDate(toDateLong);
        assertEquals(toDateLong, statisticalDataQueryCriteria.getToDate().longValue());
    }

    private StatisticalDataQueryCriteria getExampleStatisticalDataQueryCriteriaWithDatapoints(String... datapoints) {
        // Some random values which don't matter for this test.
        final String deviceId = "123";
        final Date fromDate = new Date();
        final Date toDate = new Date(fromDate.getTime() + 1000L);
        final StatisticalDataQueryCriteria.Grouping grouping = StatisticalDataQueryCriteria.Grouping.Day;
        return new StatisticalDataQueryCriteria(deviceId, grouping, fromDate, toDate, datapoints);
    }

    private StatisticalDataQueryCriteria getExampleStatisticalDataQueryCriteriaWithFromDate(Date fromDate) {
        // Some random values which don't matter for this test.
        final String deviceId = "123";
        final Date toDate = new Date(fromDate.getTime() + 1000L);
        final StatisticalDataQueryCriteria.Grouping grouping = StatisticalDataQueryCriteria.Grouping.Day;
        final String datapoint = "Test";
        return new StatisticalDataQueryCriteria(deviceId, grouping, fromDate, toDate, datapoint);
    }

    private StatisticalDataQueryCriteria getExampleStatisticalDataQueryCriteriaWithFromDate(long fromDate) {
        // Some random values which don't matter for this test.
        final String deviceId = "123";
        final long toDate = fromDate + 1000L;
        final StatisticalDataQueryCriteria.Grouping grouping = StatisticalDataQueryCriteria.Grouping.Day;
        final String datapoint = "Test";
        return new StatisticalDataQueryCriteria(deviceId, grouping, fromDate, toDate, datapoint);
    }

    private StatisticalDataQueryCriteria getExampleStatisticalDataQueryCriteriaWithToDate(Date toDate) {
        // Some random values which don't matter for this test.
        final String deviceId = "123";
        final Date fromDate = new Date(toDate.getTime() - 1000L);
        final StatisticalDataQueryCriteria.Grouping grouping = StatisticalDataQueryCriteria.Grouping.Day;
        final String datapoint = "Test";
        return new StatisticalDataQueryCriteria(deviceId, grouping, fromDate, toDate, datapoint);
    }

    private StatisticalDataQueryCriteria getExampleStatisticalDataQueryCriteriaWithToDate(long toDate) {
        // Some random values which don't matter for this test.
        final String deviceId = "123";
        final long fromDate = toDate - 1000L;
        final StatisticalDataQueryCriteria.Grouping grouping = StatisticalDataQueryCriteria.Grouping.Day;
        final String datapoint = "Test";
        return new StatisticalDataQueryCriteria(deviceId, grouping, fromDate, toDate, datapoint);
    }
}
