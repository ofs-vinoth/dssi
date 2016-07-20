package com.thecdmgroup.driiverseatservice.domain;

import java.util.List;

import com.thecdmgroup.driiverseatservice.common.ExceptionInfo;

/**
 * @author vinothn
 *
 */
public class ResponseObject {

	private String id;
	private String status;
	private String description;
	private List<ExceptionInfo> errors;

	/**
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return errors
	 */
	public List<ExceptionInfo> getErrors() {
		return errors;
	}

	/**
	 * 
	 * @param errors
	 */
	public void setErrors(List<ExceptionInfo> errors) {
		this.errors = errors;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return " ID "  + this.id + " " + " Status "  + this.status + " description "  + this.description;
	}

}
