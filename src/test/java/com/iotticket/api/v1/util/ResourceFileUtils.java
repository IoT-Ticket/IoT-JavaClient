package com.iotticket.api.v1.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

public class ResourceFileUtils {
	
	/**
	 * Reads resource file contents to String.
	 * @param path Path to resource file. For example 
	 * 	"com/iotticket/api/v1/resource.json"
	 * @return Resource file contents as String.
	 * @throws IllegalStateException Is thrown if resource file is not found.
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	
	public static String resourceFileToString(String path) throws IOException, URISyntaxException {
		InputStream resourceFileStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		
		if (resourceFileStream == null) {
			String message = String.format("Resource file %s not found.", path);
			throw new IllegalStateException(message);
		}
		
		Scanner scanner = new Scanner(resourceFileStream);
		StringBuilder buffer = new StringBuilder();
		try {
			while( scanner.hasNextLine() ) {
				buffer.append( scanner.nextLine() );
			}
		} finally {
			scanner.close();
		}
		return buffer.toString();
	}
	
}
