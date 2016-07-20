package com.thecdmgroup.driiverseatservice.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;
import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.rest.util.RESTMessage.Status;
import com.thecdmgroup.driiverseatservice.util.DriiverSeatUtil;
import com.thecdmgroup.driiverseatservice.util.PropertyUtil;

import net.jmatrix.utils.DebugUtils;
import net.jmatrix.utils.PerfTrack;

/**
 * REST wrapper which helps driiverseat server layer to communicate with SOA REST
 * services.
 * 
 * @author vinothn 
 *  
 */
public class RESTDelegator implements DriverSeatConstants {
	
	@Autowired
	PropertyUtil pUtil;
	
	private static final Logger log = LogManager.getLogger(RESTDelegator.class);
	private static final String BACK_SLASH = "/";

	private static RESTDelegator singleton;

	private final static String RESTURL = "veeva.rs.url";
		
	/** SOA REST Header Information **/
	private final static String HEADER_SESSION = "X-JSession";
	private final static String HEADER_TRANSACTION = "X-Transaction";

	private RESTDelegator() {
		super();
	}

	/**
	 * Get web resource
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	private WebResource getWebResource() {
		//PAPSession papSession = (PAPSession)LogContext.get(SESSION);
		WebResource webResource = null;
		//if (PAPSession.getWebResource() == null) { 
			//ClientConfig cc = new DefaultClientConfig();
		    // Use the ApacheHttpClient since it can be configured to manage cookies
		    // thus maintaining session
	    DefaultApacheHttpClientConfig cc = new DefaultApacheHttpClientConfig(); 
	    cc.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true); 
	    cc.getClasses().add(JacksonJsonProvider.class);
		//Once CAAPI 0.3.18 is deployed add cc to create as argument below
		//Client client = Client.create(cc);
	    ApacheHttpClient client = ApacheHttpClient.create(cc);
		client.setConnectTimeout(new Integer(1000 * 60 * 45));
		client.setReadTimeout(new Integer(1000 * 60 * 45));
		
		webResource = client
				.resource(getBaseURI(getPropertyValue(RESTURL)));
		log.info("SOA URL: " + getPropertyValue(RESTURL));
			//papSession.setWebResource(webResource);
		//} 
		
		//webResource = (WebResource)papSession.getWebResource();
		
		
		return webResource;
	}

	/**
	 * Returns the property value from rest.properties file for specified key
	 * returns empty string when the key does not exist in the file
	 * 
	 * @param key
	 * @return
	 */
	private String getPropertyValue(String key) {
		return pUtil.getProperty(key);
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {

		log.info("Initializing the REST Delegator.");

		log.info("REST Service URL loaded... " + getPropertyValue(RESTURL));
	}

	/**
	 * Gets the base uri.
	 * 
	 * @return the base uri
	 */
	protected static URI getBaseURI(String uri) {
		UriBuilder builder = UriBuilder.fromUri(uri);
		URI _uri = builder.build();
		return _uri;
		//return UriBuilder.fromUri(uri).port(8080).build();
	}
	
	/**
	 * 
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public Object getJSONStringAsObject(String jsonString, Class<?> clazz) {
		
		if (jsonString == null || jsonString.trim().length() == 0) return null;
		
		log.info("Start: getJSONStringAsObject(Object o) ");
		ObjectMapper mapper = new ObjectMapper();
		Object object =  null;
		try {
			object = mapper.readValue(jsonString, clazz);
		} catch (Exception ex) {
			log.info("Error: getJSONStringAsObject(Object o) ");
			log.error(DebugUtils.stackString(ex));
		}
		
		log.info("Finish: getJSONStringAsObject(Object o) ");
		
		return object;
	}
	
	/**
	 * Handles the pap server's REST request and fetching from or posting data
	 * into SOA Layer
	 * 
	 * @param message
	 *            holds pap request information
	 * 
	 * @return Returns response RESTMessage.
	 */
	public RESTMessage handle(RESTMessage message) throws ServiceException {
		return handle(message, null, null);
	}

	/**
	 * This handle method validates the RESTMessag and initiates send call for
	 * REST service
	 * 
	 * @param message
	 * @param sId
	 * @param txId
	 * @return
	 * @throws ServiceException
	 */
	public RESTMessage handle(RESTMessage message, String sId, String txId)
			throws ServiceException {

		String perfLogString = "handle(message," + sId +", "+txId +") for REST URL " + message.getMethodName();
		
		PerfTrack.start(perfLogString);
		try {
			validateMessage(message);
			String path = constructRESTPath(message);
			message = sendRequest(message, path, sId, txId);
			message.setCallStatus(Status.OK);
			message.setErrorMesssage(null);
		} catch(UniformInterfaceException uie) {
			log.info("ERROR: handle(RESTMessage message, String sId, String txId) for "
					+ message.getMethodName());
//			log.error(DebugUtils.stackString(uie));	
			
			rethrowAsServiceException(uie);
		} catch (Exception ex) {
			message.setCallStatus(Status.ERROR);
			message.setErrorMesssage(ex.getMessage());
			log.info("ERROR: handle(RESTMessage message, String sId, String txId) for "
					+ message.getMethodName());
//			log.error(DebugUtils.stackString(ex));

			throw new ServiceException("Error processing handle REST request "
					+ ex.getMessage(), ex);
		} finally {
			PerfTrack.stop(perfLogString);	
		} 
		
		
		log.info("Finish: handle(RESTMessage message, String sId, String txId) ");
		return message;
	}
	
	private void convertToObject(RESTMessage message, Object response)
			throws Exception {
		
		log.info("Start: convertToObject(RESTMessage message, Object response)");
		log.debug("SOA Input for " + message.getMethodName());
		log.debug(DriiverSeatUtil.getAsJSONString(message.getInfo()));
		log.debug("SOA Response for " + message.getMethodName());
		log.debug(response.toString());
		

		if (message.getAcceptClass().equals(String.class)) {
			message.setResponseData(response);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			Object o = mapper.readValue(response.toString(),
					message.getAcceptClass());
			message.setResponseData(o);
		}
		log.info("Finish: convertToObject(RESTMessage message, Object response)");
	}

	/**
	 * This method handles the GET method REST service for a specific return
	 * type class and generic type such as List<?>, Map<K,V> etc based on what
	 * the RESTMessage object has in it
	 * 
	 * @param builder
	 * @param message
	 */
	private void handleGET(Builder builder, RESTMessage message)
			throws Exception {

		log.info("Start: handleGET(Builder builder, RESTMessage message)");
		Object response;
		if (message.getGenericType() != null) {
			response = builder.get(message.getGenericType());
			message.setResponseData(response);
		} else {
			response = builder.get(String.class);
			convertToObject(message, response);
		}
		log.info("Finish: handleGET(Builder builder, RESTMessage message)");
	}

	/**
	 * This method handles the PUT method on REST service for a specific return
	 * type class and generic type such as List<?>, Map<K,V> etc based on what
	 * the RESTMessage object has in it
	 * 
	 * @param builder
	 * @param message
	 */
	private void handlePUT(Builder builder, RESTMessage message) {

		log.info("Start: handlePUT(Builder builder, RESTMessage message)");
		Class<?> acceptClass = message.getAcceptClass();
		GenericType<?> genericType = message.getGenericType();
		Object response = null;

		if (acceptClass == null & genericType == null) {
			if (message.getInfo() != null)
				builder.put(message.getInfo());
			else
				builder.put();
		} else if (acceptClass != null) {
			if(message.getInfo() != null)
				response = builder.put(message.getAcceptClass(), message.getInfo());
			else
				response = builder.put(message.getAcceptClass());
		} else if (genericType != null) {
			if (message.getInfo() != null)
				response = builder.put(message.getGenericType(), message.getInfo());
			else
				response = builder.put(message.getGenericType());
		}

		message.setResponseData(response);
		log.info("Finish: handlePUT(Builder builder, RESTMessage message)");
	}

	/**
	 * This method handles the POST method on REST service for a specific return
	 * type class and generic type such as List<?>, Map<K,V> etc based on what
	 * the RESTMessage object has in it
	 * 
	 * @param builder
	 * @param message
	 */
	private void handlePOST(Builder builder, RESTMessage message)
			throws Exception {

		log.info("Start: handlePOST(Builder builder, RESTMessage message)");
		Class<?> acceptClass = message.getAcceptClass();
		GenericType<?> genericType = message.getGenericType();
		Object response = null;

		if (acceptClass == null & genericType == null) {
			if (message.getInfo() != null)
				builder.post(message.getInfo());
			else
				builder.post();
				
		} else if (acceptClass != null) {
			if (message.getInfo() != null)
				response = builder.post(String.class, message.getInfo());
			else 
				response = builder.post(String.class);
			convertToObject(message, response);
		} else if (genericType != null) {
			if (message.getInfo() != null)
				response = builder
						.post(message.getGenericType(), message.getInfo());
			else
				response = builder.post(message.getGenericType());
		}
		log.info("Finish: handlePOST(Builder builder, RESTMessage message)");
	}

	/**
	 * This method handles the DELETE method on REST service for a specific
	 * return type class and generic type such as List<?>, Map<K,V> etc based on
	 * what the RESTMessage object has in it
	 * 
	 * @param builder
	 * @param message
	 */
	private void handleDELETE(Builder builder, RESTMessage message)
			throws Exception {

		log.info("Start: handleDELETE(Builder builder, RESTMessage message)");
		Class<?> acceptClass = message.getAcceptClass();
		GenericType<?> genericType = message.getGenericType();
		Object response = null;

		if (acceptClass == null & genericType == null) {
			if (message.getInfo() != null)
				builder.delete(message.getInfo());
			else
				builder.delete();
		} else if (acceptClass != null) {
			if (message.getInfo() != null)
				response = builder.delete(String.class, message.getInfo());
			else
				response = builder.delete(String.class);
			convertToObject(message, response);
		} else if (genericType != null) {
			if (message.getInfo() != null)			
				response = builder.delete(message.getGenericType(),
						message.getInfo());
			else
				response = builder.delete(message.getGenericType());
		}
		log.info("Finish: handleDELETE(Builder builder, RESTMessage message)");
	}

	/**
	 * Sends the pap request to SOA layer.
	 * 
	 * @param message
	 *            holds pap request information
	 * @param webResource
	 *            holds web resource
	 */
	private RESTMessage sendRequest(RESTMessage message, String path,
			String sId, String txId) throws Exception {
		
		log.info("Start: sendRequest(RESTMessage message, " + path + ", " + sId + ", " + txId + ")");
		WebResource webResource = getWebResource();
		Builder builder;

		builder = webResource.path(path).type(message.getInputType());
		builder = builder.accept(message.getAcceptType());

		constructHeaderInfo(builder, sId, txId);
		
		if (RESTMessage.MethodType.GET_WITH_PATH_PARAM == message
				.getMethodType()) {

			String newPath = path;
			if (message.getInfo() != null) {
				newPath += BACK_SLASH + message.getInfo();
			}
			
			builder = getWebResource().path(newPath).type(
					message.getInputType());
			
			builder = builder.accept(message.getAcceptType());

			constructHeaderInfo(builder, sId, txId);
			handleGET(builder, message);

		} else if (RESTMessage.MethodType.GET_WITH_QUERY_PARAM == message
				.getMethodType()) {
			//TODO  ??

		} else if (RESTMessage.MethodType.PUT == message.getMethodType()) {
			handlePUT(builder, message);

		} else if (RESTMessage.MethodType.POST == message.getMethodType()) {
			handlePOST(builder, message);

		} else if (RESTMessage.MethodType.DELETE == message.getMethodType()) {
			handleDELETE(builder, message);
		}

//		ClientResponse cResponse = builder.head();
//		List<NewCookie> cookies = cResponse.getCookies();
		
		log.info("Finish: sendRequest(RESTMessage message,String path, String sId, String txId)");
		return message;
	}

	/**
	 * Constructs the service and method as URL for REST invocation.
	 * 
	 * @param message
	 *            holds pap request information
	 * @throws ServiceException
	 */
	private String constructRESTPath(RESTMessage message)
			throws ServiceException {

		log.info("Start: constructRESTPath(RESTMessage message)");
		String serviceName = message.getServiceName();
		String restPath = BACK_SLASH + serviceName + BACK_SLASH
				+ message.getMethodName();
		log.info("Finish: constructRESTPath(RESTMessage message)");
		return restPath;

	}


	/**
	 * Constructs the header information for the REST service Current header
	 * information contains following <li>Session Id</li> <li>Transaction Id</li>
	 * 
	 * @throws ServiceException
	 */
	private void constructHeaderInfo(Builder builder, String sId, String txId)
			throws ServiceException {

		String sessionId = (String)ThreadContext.get(DriverSeatConstants.SESSION_ID); //PAPSession.getSessionId();
		String transactionId = (String)ThreadContext.get(DriverSeatConstants.REQUEST_ID); //generateRequestID(sessionId);

		if (sId == null && txId == null) {
			builder.header(HEADER_SESSION, sessionId);
			builder.header(HEADER_TRANSACTION, transactionId);
		} else {
			builder.header(HEADER_SESSION, sId);
			builder.header(HEADER_TRANSACTION, txId);
		}
	}
	
	/**
	 * Validates the mandatory input data of the request message.
	 * 
	 * @param message
	 *            holds pap request information
	 * @throws ServiceException
	 */
	private void validateMessage(RESTMessage message) throws ServiceException {

		log.info("Start: validateMessage(RESTMessage message)");

		if (message == null) {
			log.fatal("Invalid request.. Try again..");
			throw new ServiceException("Invalid request.. Try again..");
		} else {

			if (message.getServiceName() == null) {
				log.fatal("Requested service name is empty");
				throw new ServiceException("Requested service name is empty");
			}

			if (message.getMethodName() == null) {
				log.fatal("Requested method name is empty");
				throw new ServiceException("Requested method name is empty");
			}

			if (message.getMethodType() == null) {
				log.error("Requested method type is empty");
				log.info("Error: validateMessage(RESTMessage message)");
				throw new ServiceException("Requested method type is empty");
			}
		}
		log.info("Finish: validateMessage(RESTMessage message)");
	}
	
	
	/**
	 * This method extracts the exception message from the UniformInterfaceException
	 * (excepting 'code: ' in the beginning of the exception message) 
	 * and throws ServiceException with the extracted exception message.
	 * 
	 * @param uie
	 * @throws ServiceException
	 */
	public void rethrowAsServiceException(UniformInterfaceException uie) throws ServiceException {
		InputStream errorStream = uie.getResponse().getEntityInputStream();
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(errorStream, writer);
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		String errorString = writer.toString();
		if (errorString != null && errorString.length() == 0) {
			throw new ServiceException(uie.getMessage(), uie);
		}
		int errorMsgStartIndex = errorString.indexOf("code:");
		int errorMsgEndIndex;
		
		if(errorMsgStartIndex == -1) {
			throw new ServiceException(errorString, uie);
		}
		
		errorMsgEndIndex = errorString.indexOf("\n", errorMsgStartIndex);
		log.fatal(errorString); //Logging the entire error String
		errorString = errorString.substring(errorMsgStartIndex, errorMsgEndIndex);
		errorMsgStartIndex = errorString.indexOf("Exception:");
		if (errorMsgStartIndex != -1) {
			errorMsgStartIndex += "Exception:".length();
			errorString = errorString.substring(errorMsgStartIndex);
		}
		
		errorString.replaceAll("&lt;br/&gt;", "\n");		
		
		throw new ServiceException(errorString, uie);
	}

}
