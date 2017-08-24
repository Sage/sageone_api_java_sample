package org.sage_one_sample.sageone;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SageoneConfigs {
	
	private static final SageoneConfigs INSTANCE = new SageoneConfigs();
	
	public static SageoneConfigs getInstance() {
		return INSTANCE;
	}
	
	private Properties configs;
	
	private SageoneConfigs() {
		try (FileInputStream in = new FileInputStream("./application.properties")) {
			configs.load(in);
		} catch (IOException ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
	
	
	public String getProperty(String propertyName) {
		String value = configs.getProperty(propertyName);
		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException(String.format("Property \"%s\" not configured in your \"application.properties\" file", propertyName));
		}
		return value;
	}
	
	
	
	
	

}
