package com.thecdmgroup.driiverseatservice.rest.util;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author hkrishnanarasimhulu
 * @since March 22, 2013
 */
public class RESTURLUtil {

	static final Logger log = LogManager.getLogger(RESTURLUtil.class);
	private static String REST_URL;

	/**
	 * 
	 * @throws IOException
	 */
	public static void initialize(String restUrl) throws IOException {
		REST_URL = restUrl;
	}

	public static String getRESTUrl() {
		return REST_URL;
	}
}
