package com.thecdmgroup.driiverseatservice.rest.util;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.GenericType;

/**
 * @author vinothn
 */
public class RESTMessage {

	public enum MethodType {
		GET_WITH_QUERY_PARAM, GET_WITH_PATH_PARAM, PUT, POST, DELETE;
	}

	public enum Status {
		OK, ERROR
	}

	private String serviceName;
	private String methodName;
	private Object info;
	private MethodType methodType;
	private MediaType acceptType;
	private Class<?> acceptClass;
	private MediaType inputType;
	private Status callStatus;
	private String errorMesssage;
	private Object responseData;
	private GenericType<?> genericType;

	private String sId;
	private String txnId;

	public GenericType<?> getGenericType() {
		return genericType;
	}

	public void setGenericType(GenericType<?> genericType) {
		this.genericType = genericType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	public MethodType getMethodType() {
		return methodType;
	}

	public void setMethodType(MethodType methodType) {
		this.methodType = methodType;
	}

	public MediaType getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(MediaType acceptType) {
		this.acceptType = acceptType;
	}

	public Class<?> getAcceptClass() {
		return acceptClass;
	}

	public void setAcceptClass(Class<?> acceptClass) {
		this.acceptClass = acceptClass;
	}

	public MediaType getInputType() {
		return inputType;
	}

	public void setInputType(MediaType inputType) {
		this.inputType = inputType;
	}

	public Status getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Status newCallStatus) {
		this.callStatus = newCallStatus;
	}

	public String getErrorMesssage() {
		return errorMesssage;
	}

	public void setErrorMesssage(String errorMesssage) {
		this.errorMesssage = errorMesssage;
	}

	public String getsId() {
		return sId;
	}

	public void setsId(String sId) {
		this.sId = sId;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	/**
	 * Generic method that creates skeleton of RESTMessage object with
	 * parameters passed to
	 * <li>constructMessageForLeadService</li>
	 * <li>constructMessageForQuoteService</li>
	 * <li>constructMessageForLeadService</li>
	 * <li>constructMessageForLeadService</li>
	 * <li>constructMessageForLeadService</li>
	 * 
	 * 
	 * @param method
	 * @param info
	 * @param acceptClass
	 * @param genericType
	 * @param methodType
	 * @return
	 */
	public static RESTMessage constructRESTMessage(String method, Object info, Class<?> acceptClass,
			GenericType<?> genericType, RESTMessage.MethodType methodType, String serviceName) {

		RESTMessage message = new RESTMessage();
		message.setMethodName(method);
		message.setInfo(info);
		message.setMethodType(methodType);
		message.setAcceptClass(acceptClass);
		message.setGenericType(genericType);
		message.setServiceName(serviceName);

		message.setInputType(MediaType.APPLICATION_JSON_TYPE);
		message.setAcceptType(MediaType.APPLICATION_JSON_TYPE);

		return message;

	}
}
