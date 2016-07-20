/**
 * 
 */
package com.thecdmgroup.driiverseatservice.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vinothn
 *
 */
public class NewlyAddedObjects {

	private List<String> ids = new ArrayList<String>();
	private String status;
	private String description = "";
	private String errors = "";

	/**
	 * @return the errors
	 */
	public String getErrors() {
		return errors;
	}

	/**
	 * @param errors
	 *            the errors to set
	 */
	public void setErrors(String errors) {
		this.errors += errors;
	}

	/**
	 * @return the ids
	 */
	public List<String> getIds() {
		return ids;
	}

	/**
	 * @return
	 */
	public String[] getIdsAsArray() {
		return ids.toArray(new String[ids.size()]);
	}

	/**
	 * @param id
	 */
	public void addId(String id) {

		ids.add(id);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
