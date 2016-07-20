/**
 * 
 */
package com.thecdmgroup.driiverseatservice.domain;

import java.util.Map;

/**
 * @author vinothn
 *
 */
public class VeevaiDetailMetaData {

	private String productName;
	private String productId;
	private String description;
	
	private Map<String, String> keyMessages;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the keyMessages
	 */
	public Map<String, String> getKeyMessages() {
		return keyMessages;
	}

	/**
	 * @param keyMessages the keyMessages to set
	 */
	public void setKeyMessages(Map<String, String> keyMessages) {
		this.keyMessages = keyMessages;
	}
	
}
