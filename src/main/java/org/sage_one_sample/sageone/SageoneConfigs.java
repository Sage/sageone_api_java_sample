package org.sage_one_sample.sageone;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SageoneConfigs {
	
	private static final SageoneConfigs INSTANCE = new SageoneConfigs();
	
	public static SageoneConfigs getInstance() {
		return INSTANCE;
	}
	
	private Properties configs = new Properties();
	
	private SageoneConfigs() {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		if (in != null) {
			try {
				configs.load(in);
			} catch (IOException ex) {
				throw new RuntimeException(ex.getMessage(), ex);
			}
		} else {
			throw new RuntimeException(">>>> Please, make a copy from the file\n\t\"/src/main/resources/application-sample.properties\" to\n\t\"/src/main/resources/application.properties\" and adjust the parameters accordingly to your application\n\n");
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
