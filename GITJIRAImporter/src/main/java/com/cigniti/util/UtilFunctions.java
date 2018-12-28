package com.cigniti.util;

import java.io.*;
import java.util.Properties;

public class UtilFunctions {
	
	public static Properties loadApiSettings() throws IOException {		
		Properties prop = new Properties();
		String propFilename = "onedriveapi_settings.properties"; 
		 
		InputStream inputStream = UtilFunctions.class.getClassLoader().getResourceAsStream(propFilename);		 
		if (inputStream != null) {
			prop.load(inputStream);
		} else {			
			throw new FileNotFoundException("property file '" + propFilename + "' not found in the classpath");
		}
		
		return prop;
	}

}
