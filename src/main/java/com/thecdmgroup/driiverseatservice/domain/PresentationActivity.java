/**
 * 
 */
package com.thecdmgroup.driiverseatservice.domain;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.thecdmgroup.driiverseatservice.rest.util.DateJsonDeserializer;
import com.thecdmgroup.driiverseatservice.rest.util.DateJsonSerializer;

/**
 * @author vinothn
 *
 */
public class PresentationActivity {

	
	private String hcpVRep;
	private String presentationName;
	private String eventType;
    private String pageName;
    private String pageID;									//Veeva-Salesforce Presentation
	private String category;
	private String action;
	private String label;
	private Date eventStartTime;
	private Date eventEndTime;
	private String driiUID;
	private String deviceName;
	private String performedBy;

	
	/**
	 * @return the hcpVRep
	 */
	public String getHcpVRep() {
		return hcpVRep;
	}

	/**
	 * @param hcpVRep
	 *            the hcpVRep to set
	 */
	public void setHcpVRep(String hcpVRep) {
		this.hcpVRep = hcpVRep;
	}

	/**
	 * @return the presentationName
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * @param presentationName
	 *            the presentationName to set
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * @return the eventType
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	
    /**
	 * @return the pageName
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * @param pageName
	 *            the pageName to set
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * @return the pageID
	 */
	public String getPageID() {
		return pageID;
	}

	/**
	 * @param pageID the pageID to set
	 */
	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
    /**
     *  
     * @return eventStartTime
     */
	
	@JsonSerialize(using = DateJsonSerializer.class)
	public Date getEventStartTime() {
		return eventStartTime;
	}
	
	/**
	 * 
	 * @param eventStartTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}
	
	/**
	 * 
	 * @return eventEndTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public Date getEventEndTime() {
		return eventEndTime;
	}
	
	/**
	 * 
	 * @param eventEndTime
	 */
	@JsonSerialize(using = DateJsonSerializer.class)
	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
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
	 * @return the deviceName
	 */
	public String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the performedBy
	 */
	public String getPerformedBy() {
		return performedBy;
	}

	/**
	 * @param performedBy the performedBy to set
	 */
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

}
