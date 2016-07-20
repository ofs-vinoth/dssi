package com.thecdmgroup.driiverseatservice.domain;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.thecdmgroup.driiverseatservice.rest.util.DateJsonSerializer;


/**
 * @author vinothn
 *
 */
public class AppActivity {	
	
	
	private String driiUID;
	private Date eTime;
	private Date sTime;
	private String action;
	private String Label;
	private String associateHcp;
	
	
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
	 * @return action
	 */
	public String getAction() {
		return action;
	}
	
	/**
	 * 
	 * @param action
	 */
	public void setAction(String action) {
		this.action = action;
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
	 * 
	 * @return Label
	 */
	public String getLabel() {
		return Label;
	}
    
	/**
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		Label = label;
	}
    
	/**
	 * 
	 * @return associateHcp
	 */
	public String getAssociateHcp() {
		return associateHcp;
	}
    
	/**
	 * 
	 * @param associateHcp
	 */
	public void setAssociateHcp(String associateHcp) {
		this.associateHcp = associateHcp;
	}
	
	
	

}
