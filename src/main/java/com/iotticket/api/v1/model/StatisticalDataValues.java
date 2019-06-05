package com.iotticket.api.v1.model;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.annotations.SerializedName;

public class StatisticalDataValues {
	
    @SerializedName("href")
    private URI Uri;

    @SerializedName("datanodeReads")
    private Collection<StatisticalDataRead> statisticalDataRead = new ArrayList<StatisticalDataRead>();

    public URI getUri() {
        return Uri;
    }

    public void setUri(URI uri) {
        Uri = uri;
    }

    public Collection<StatisticalDataRead> getStatisticalDataRead() {
        return statisticalDataRead;
    }	
	
	public static class StatisticalDataRead extends DatanodeBase {
        
		@SerializedName("values")
        private Collection<StatisticalDataReadValue> statisticalDataReadValues = new ArrayList<StatisticalDataReadValue>();


        public Collection<StatisticalDataReadValue> getStatisticalDataReadValues() {
            return statisticalDataReadValues;
        }
	}
	
	public static class StatisticalDataReadValue {
		
		@SerializedName("min")
		private String minimum;
		
		@SerializedName("max")
		private String maximum;
		
		@SerializedName("avg")
		private String average;
		
		private long count;
		
		private String sum;
				
		@SerializedName("ts")
		private long timestampMilliSeconds;
		
		public String getMinimum() {
			return minimum;
		}
		
		public void setMinimum(String minimum) {
			this.minimum = minimum;
		}
		
		public String getMaximum() {
			return maximum;
		}
		
		public void setMaximum(String maximum) {
			this.maximum = maximum;
		}
		
		public String getAverage() {
			return average;
		}
		
		public void setAverage(String average) {
			this.average = average;
		}
		
		public long getCount() {
			return count;
		}
		
		public void setCount(long count) {
			this.count = count;
		}
		
		public String getSum() {
			return sum;
		}
		
		public void setSum(String sum) {
			this.sum = sum;
		}
		
		public long getTimestampMilliSeconds() {
			return timestampMilliSeconds;
		}
		
		public void setTimestampMilliSeconds(long timestampMilliSeconds) {
			this.timestampMilliSeconds = timestampMilliSeconds;
		}
		
		// TODO: Object->getConvertedValue
		// TODO: Should we have data type also in here (see: datanodereadvalue)
	}
	
	
}
