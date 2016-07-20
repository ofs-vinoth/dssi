/**
 * 
 */
package com.thecdmgroup.driiverseatservice.rest.util;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.sforce.soap.enterprise.fault.ApiFault;
import com.thecdmgroup.driiverseatservice.common.ExceptionInfo;

/**
 * @author vinothn
 *
 */
@Provider
@Component
public class DriiverSeatServiceExceptionMapper implements ExceptionMapper<Exception> {
	private static final transient ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public Response toResponse(final Exception exception) {
		ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR).entity(defaultJSON(exception))
				.type(MediaType.APPLICATION_JSON);
		return builder.build();
	}

	private String defaultJSON(final Exception exception) {
		
		Status status = Status.INTERNAL_SERVER_ERROR;
		String msg = Status.INTERNAL_SERVER_ERROR.name();
		String desc = exception.getMessage();
		if (exception.getCause() instanceof ApiFault) {
			
			ApiFault fault = (ApiFault) exception.getCause();
			msg = fault.getExceptionCode().name();
			desc = fault.getExceptionMessage();
		} else if (exception instanceof AuthenticationException) {
			
			status = Status.UNAUTHORIZED;
			msg = Status.UNAUTHORIZED.name();
		}
		
		ExceptionInfo errorInfo = new ExceptionInfo(status.getStatusCode() , msg, desc);

		try {
			return MAPPER.writeValueAsString(errorInfo);
		} catch (IOException e) {
			return "{\"message\":\"An internal error occurred\"}";
		}
	}
}
