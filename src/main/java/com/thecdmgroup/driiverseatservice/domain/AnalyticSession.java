package com.thecdmgroup.driiverseatservice.domain;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.thecdmgroup.driiverseatservice.rest.util.DateJsonSerializer;

public class AnalyticSession {
	
	private String appName;
	private String appVersion;
	private String bundleIdentifier; 
	private String deviceVersion;
	private String deviceName;
	private String driiUID;
	private String clientName;
	private Date eTime;
	private Date sTime;
	
	//Veeva Domain Analytics Data
	private String repId;													    // Salesforce Account_vod__c Login User ID
	private String productId;												    // Salesforce Product Detail_vod__c
	private String productName;												    // Salesforce Product Detailed_Products_vod__c
	private String keyMessages;												   	// Salesforce Call assoiciatekeyMessages
	
	/**
	 * 
	 * @return appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * 
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * 
	 * @return appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}
	/**
	 * 
	 * @param appVersion
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	/**
	 * 
	 * @return bundleIdentifier
	 */
	public String getBundleIdentifier() {
		return bundleIdentifier;
	}
	/**
	 * 
	 * @param bundleIdentifier
	 */
	public void setBundleIdentifier(String bundleIdentifier) {
		this.bundleIdentifier = bundleIdentifier;
	}
	/**
	 * 
	 * @return deviceVersion
	 */
	public String getDeviceVersion() {
		return deviceVersion;
	}
	/**
	 * 
	 * @param deviceVersion
	 */
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}
	/**
	 * 
	 * @return deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}
	/**
	 * 
	 * @param deviceName
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	/**
	 * 
	 * @return driiUID
	 */
	public String getDriiUID() {
		return driiUID;
	}
	/**
	 * 
	 * @param driiUID
	 */
	public void setDriiUID(String driiUID) {
		this.driiUID = driiUID;
	}
	/**
	 * 
	 * @return clientName
	 */
	public String getClientName() {
		return clientName;
	}
	/**
	 * 
	 * @param clientName
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	/**
	 * 
	 * @return eTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public Date geteTime() {
		return eTime;
	}
    
	/**
	 * 
	 * @param eTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	
	/**
	 * 
	 * @return sTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public Date getsTime() {
		return sTime;
	}
    
	/**
	 * 
	 * @param sTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	/**
	 * @return the repId
	 */
	public String getRepId() {
		return repId;
	}
	/**
	 * @param repId the repId to set
	 */
	public void setRepId(String repId) {
		this.repId = repId;
	}
	/**
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}
	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	/**
	 * @return the keyMessages
	 */
	public String getKeyMessages() {
		return keyMessages;
	}
	/**
	 * @param keyMessages the keyMessages to set
	 */
	public void setKeyMessages(String keyMessages) {
		this.keyMessages = keyMessages;
	}

}
