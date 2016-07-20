/**
 * 
 */
package com.thecdmgroup.driiverseatservice.service;

import javax.annotation.security.RolesAllowed;

import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.VeevaiDetailMetaData;

/**
 * @author vinothn
 *
 */
public interface VeevaiDetailService {

	/**
	 * @param iDetatilName
	 * @return
	 * @throws ServiceException 
	 */
	@RolesAllowed({"REP"})
	public VeevaiDetailMetaData getVeevaiDetailMetaData(String iDetatilName) throws ServiceException;
}
