/**
 * 
 */
package com.thecdmgroup.driiverseatservice.common;

/**
 * @author vinothn
 *
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(String message, Exception ex) {
		 super(message, ex);
	}

	public ServiceException(String message) {
		 super(message);
	}

	public ServiceException(Exception e) {
		 super(e);
	}

}
