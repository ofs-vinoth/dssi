/**
 * 
 */
package com.thecdmgroup.driiverseatservice.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.sforce.soap.enterprise.fault.ApiFault;
import com.sforce.ws.ConnectionException;
import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.User;
import com.thecdmgroup.driiverseatservice.service.UserService;
import com.thecdmgroup.driiverseatservice.transport.jaxws.SalesforceProxyImpl;
import com.thecdmgroup.driiverseatservice.util.PropertyUtil;

/**
 * @author vinothn
 *
 */
public class UserServiceImpl implements UserService {

	private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

	/*@Autowired
	private RESTDelegator delegator;*/

	@Autowired
	private SalesforceProxyImpl proxy;
	
	@Autowired
	PropertyUtil pUtil;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thecdmgroup.driiverseatservice.service.UserService#login(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	@Override
	public User login(String username, String password, String env) throws ServiceException {

		User user = null;
		try {
			String enviroment = DriverSeatConstants.ENV_PROD.equals(env) ? DriverSeatConstants.VEEVA_WS_URL_PROD :
																			DriverSeatConstants.VEEVA_WS_URL_TEST;
			String veevaWSURL = pUtil.getProperty(enviroment);
			proxy.createConection(username, password, veevaWSURL);
			user = proxy.getLoggedInUser();

		} catch (ConnectionException ce) {

			log.debug("Connection Exception" + ce);
			throw new ServiceException(ce);
		}

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.thecdmgroup.driiverseatservice.service.UserService#getAssoicateHCPs()
	 */
	@Override
	public List<User> getAssoicateHCPs() throws ServiceException {

		List<User> hcps = new ArrayList<User>();

		try {
			hcps = proxy.getAssoicateHCPs();
		} catch (ConnectionException ce) {

			log.debug("Connection Exception" + ce);

			throw new ServiceException(ce);
		}
		// TODO Auto-generated method stub
		return hcps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.thecdmgroup.driiverseatservice.service.UserService#logout()
	 */
	@Override
	public boolean logout() throws ServiceException {

		try {
			
			boolean result = proxy.closeConnection();
			log.debug("User Logged ut :");
			return result;
		} catch (ConnectionException ce) {
			throw new ServiceException(ce);
		}
	}

	/*
	 * @Override public User loginTest(String username, String password) throws
	 * ServiceException {
	 * 
	 * User user = null; RESTMessage message =
	 * RESTMessage.constructRESTMessage("login", MediaType.APPLICATION_JSON,
	 * User.class, null, RESTMessage.MethodType.POST, "session"); Map<String,
	 * String> auth = new HashMap<>(); auth.put("user",username);
	 * auth.put("pass",password); message.setInfo(auth);
	 * 
	 * try { RESTMessage response = delegator.handle(message);
	 * 
	 * user = (User)response.getResponseData();
	 * 
	 * } catch (Exception ce) {
	 * 
	 * log.debug("Connection Exception" + ce);
	 * 
	 * if (ce instanceof ApiFault) { ApiFault fault = (ApiFault) ce; throw new
	 * ServiceException(fault.getExceptionCode().name(),
	 * fault.getExceptionMessage()); } else { throw new ServiceException(ce); }
	 * }
	 * 
	 * return user; }
	 */

}
