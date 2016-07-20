/**
 * 
 */
package com.thecdmgroup.driiverseatservice.service;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.User;

/**
 * @author vinothn
 *
 */
public interface UserService {
	
	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User login(String username, String password, String env) throws ServiceException;
	
	/**
	 * @return
	 * @throws Exception
	 */
	@RolesAllowed({"REP"})
	public List<User> getAssoicateHCPs() throws ServiceException;
	
	/**
	 * @return
	 * @throws Exception
	 */
	public boolean logout() throws ServiceException;

	//User loginTest(String username, String password) throws ServiceException;	
}
