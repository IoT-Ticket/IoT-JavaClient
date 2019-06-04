package com.iotticket.api.v1.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import com.iotticket.api.v1.exception.ValidAPIParamException;
import com.iotticket.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.api.v1.model.Device;
import com.iotticket.api.v1.model.DeviceAttribute;

public class ValidationRunnerTest {

	
	@Test
	public void testRunValidation() throws ValidAPIParamException {
		
		DeviceAttribute attribute1 = new DeviceAttribute("key1", "value1");
		DeviceAttribute attribute2 = new DeviceAttribute("key2", "value2");
		
		ArrayList<DeviceAttribute> attributes = new ArrayList<DeviceAttribute>();
		attributes.add(attribute1);
		attributes.add(attribute2);
		
		Device device = new Device();
		device.setAttributes(attributes);
		device.setName("Name");
		device.setDescription("Description");
		device.setManufacturer("Manufacturer");
		device.setType("Type");
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		validationRunner.runValidation(device);
	}
	
	
	@Test
	public void testRunValidation_requiredFieldIsNull() {
		DatanodeWriteValue datanodeWriteValue = new DatanodeWriteValue();
		datanodeWriteValue.setName("Name");
		datanodeWriteValue.setPath("/Test/Path");
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		try {
			validationRunner.runValidation(datanodeWriteValue);
			fail("Expected ValidAPIParamException.");
		} catch (ValidAPIParamException exception) {
			assertEquals("value is needed", exception.getMessage());
		}
	}
	
	
	@Test
	public void testRunValidation_requiredFieldIsEmpty() {
		DatanodeWriteValue datanodeWriteValue = new DatanodeWriteValue();
		datanodeWriteValue.setValue("");
		datanodeWriteValue.setName("Name");
		datanodeWriteValue.setPath("/Test/Path");
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		try {
			validationRunner.runValidation(datanodeWriteValue);
			fail("Expected ValidAPIParamException.");
		} catch (ValidAPIParamException exception) {
			assertEquals("value is needed", exception.getMessage());
		}
	}
	
	
	@Test
	public void testRunValidation_fieldNotMatchingWithRegex() {
		DatanodeWriteValue datanodeWriteValue = new DatanodeWriteValue();
		datanodeWriteValue.setName("Name");
		datanodeWriteValue.setValue(5);
		datanodeWriteValue.setPath("/Test/Path/");
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		try {
			validationRunner.runValidation(datanodeWriteValue);
			fail("Expected ValidAPIParamException.");
		} catch (ValidAPIParamException exception) {
			assertEquals("path attribute value /Test/Path/ is unacceptable. Check documentation", exception.getMessage());
		}
	}
	
	
	@Test
	public void testRunValidation_tooLongStringField() {
		DatanodeWriteValue datanodeWriteValue = new DatanodeWriteValue();
		datanodeWriteValue.setName("Name");
		datanodeWriteValue.setValue(5);
		datanodeWriteValue.setUnit("This unit is too long");
		datanodeWriteValue.setPath("/Test/Path/");
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		try {
			validationRunner.runValidation(datanodeWriteValue);
			fail("Expected ValidAPIParamException.");
		} catch (ValidAPIParamException exception) {
			assertEquals("unit attribute exceeds 10 characters expected", exception.getMessage());
		}
	}
	
	
	@Test
	public void testRunValidation_collectionContainsInvalidObject() throws ValidAPIParamException {
		Collection<DeviceAttribute> deviceAttributes= new ArrayList<DeviceAttribute>();
		
		DeviceAttribute validDeviceAttribute = new DeviceAttribute("key", "value");
		deviceAttributes.add(validDeviceAttribute);
		
		// Add dataNodeValue with missing value
		DeviceAttribute deviceAttributeWithMissingValue = new DeviceAttribute("key", null);
		deviceAttributes.add(deviceAttributeWithMissingValue);
		
		Device device = new Device();
		device.setName("name");
		device.setManufacturer("manufacturer");
		device.setAttributes(deviceAttributes);
		
		ValidationRunner validationRunner = new ValidationRunner();
		
		try {
			validationRunner.runValidation(device);
			fail("Expected ValidAPIParamException");
		} catch (ValidAPIParamException exception) {
			assertEquals("value is needed", exception.getMessage());
		}
		
	}
}
