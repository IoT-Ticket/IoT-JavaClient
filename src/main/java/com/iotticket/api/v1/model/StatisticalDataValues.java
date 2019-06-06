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
		private Double minimum;
		
		@SerializedName("max")
		private Double maximum;
		
		@SerializedName("avg")
		private Double average;
		
		private Long count;
		
		private Double sum;
				
		@SerializedName("ts")
		private Long timestampMilliSeconds;
		
		/**
		 * @return Minimum value, or <b>null</b> if time interval had no values.
		 */
		public Double getMinimum() {
			getClass().getClassLoader().getResource("");
			return minimum;
		}
		
		public void setMinimum(Double minimum) {
			this.minimum = minimum;
		}
		
		/**
		 * @return Maximum value, or <b>null</b> if time interval had no values.
		 */
		public Double getMaximum() {
			return maximum;
		}
		
		public void setMaximum(Double maximum) {
			this.maximum = maximum;
		}
		
		/**
		 * @return Average value, or <b>null</b> if time interval had no values.
		 */
		public Double getAverage() {
			return average;
		}
		
		public void setAverage(Double average) {
			this.average = average;
		}
		
		public Long getCount() {
			return count;
		}
		
		public void setCount(Long count) {
			this.count = count;
		}
		
		public Double getSum() {
			return sum;
		}
		
		public void setSum(Double sum) {
			this.sum = sum;
		}
		
		public Long getTimestampMilliSeconds() {
			return timestampMilliSeconds;
		}
		
		public void setTimestampMilliSeconds(Long timestampMilliSeconds) {
			this.timestampMilliSeconds = timestampMilliSeconds;
		}

	}
	
	
}
