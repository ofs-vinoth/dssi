/**
 * 
 */
package com.thecdmgroup.driiverseatservice.rest.util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * @author vinothn
 *
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		
		Date date = null;
		try {
			SimpleDateFormat d = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
			date = d.parse((parser.getText()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}

}
