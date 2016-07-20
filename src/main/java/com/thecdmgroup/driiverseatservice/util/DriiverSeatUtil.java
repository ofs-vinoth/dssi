package com.thecdmgroup.driiverseatservice.util;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;

import net.jmatrix.utils.DebugUtils;

public class DriiverSeatUtil implements DriverSeatConstants {
	
	private static final Logger log = LogManager.getLogger(DriiverSeatUtil.class);
	
	/**
	 * @param o
	 * @return
	 */
	public static String getAsJSONString(Object o) {

		if (o == null)
			return null;
		if (o instanceof String)
			return o.toString();

		log.info("Start: getAsJSON(Object o) ");
		ObjectMapper mapper = new ObjectMapper();

		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(o);
		} catch (Exception ex) {
			log.info("Error: getAsJSON(Object o) ");
			log.error(DebugUtils.stackString(ex));
		}
		log.info("Finish: getAsJSON(Object o) ");

		return jsonString;
	}

	/**
	 * @param jsonString
	 * @param acceptClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromJSON(String jsonString,
			Class<?> acceptClass) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Exception ex;
		try {
			return (T) mapper.readValue(jsonString, acceptClass);
		} catch (JsonParseException e) {
			ex = e;
			e.printStackTrace();
		} catch (JsonMappingException e) {
			ex = e;
			e.printStackTrace();
		} catch (IOException e) {
			ex = e;
			e.printStackTrace();
		}

		throw new Exception(
				"Unable to do conversion from JSON string to object", ex);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar(Date date) {

		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	/**
	 * @param earlierDate
	 * @param laterDate
	 * @return
	 */
	public static double minutesDiff(Date earlierDate, Date laterDate) {
		
	    if( earlierDate == null || laterDate == null ) return 0;

	    return (double)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
	}
}
