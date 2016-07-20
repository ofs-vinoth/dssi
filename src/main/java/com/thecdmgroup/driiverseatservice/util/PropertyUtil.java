package com.thecdmgroup.driiverseatservice.util;

import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyUtil {

	private static final Logger LOGGER = LogManager.getLogger(PropertyUtil.class);
	public String SPECIAL_CHARACTERS = "special.characters";
	private Properties props;

	public PropertyUtil(Properties props) {
		//loadProps(location);
		this.props = props;
	}
	
	/**
	 * 
	 * @param location
	 */
	private void loadProps(String location) {
		
		LOGGER.info("Loads driver seat properties from location >> " + location);
		
		try {
			props = new Properties();
			Reader reader = new FileReader(location);
			props.load(reader);
			LOGGER.info("Driver seat properties loaded successfully");
		} catch (Exception e) {
			LOGGER.info("Unable to load driver seat properties from location >> " + e);
		}
	}

	public Properties getProperties() {
		return this.props;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return (String) props.get(key);
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanProperty(String key) {
		return Boolean.getBoolean((String) props.get(key));
	}
}
