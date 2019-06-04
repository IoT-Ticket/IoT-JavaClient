package com.iotticket.api.v1.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceFileUtils {
	
	/**
	 * Reads resource file to string. Resource file must be in the same
	 * package with Class clazz.
	 * 
	 * @param filename Name of the resource file. 
	 * @param clazz Class that needs the resource. Resource file must be in the
	 * 	same package with clazz.
	 * @return File contents as strings
	 * @throws IllegalStateException Is thrown if the file is not found.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	
	// TODO: Use Thread.currentThread().getContextClassLoader().getResourceAsStream().
	public static String resourceFileToString(String filename, Class<?> clazz) throws IOException, URISyntaxException {
		URL resourceUrl = clazz.getResource(filename);
		if (resourceUrl == null) {
			throw new IllegalStateException("Resource file " + filename + " not found.");
		}
		
		Path path = Paths.get(clazz.getResource(filename).toURI());
		
		byte[] fileAsBytes = Files.readAllBytes(path);
		return new String(fileAsBytes, StandardCharsets.UTF_8.name());
	}
	
}
