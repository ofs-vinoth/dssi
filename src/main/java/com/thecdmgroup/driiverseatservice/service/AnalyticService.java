/**
 * 
 */
package com.thecdmgroup.driiverseatservice.service;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.AnalyticSession;
import com.thecdmgroup.driiverseatservice.domain.Analytics;
import com.thecdmgroup.driiverseatservice.domain.AppActivity;
import com.thecdmgroup.driiverseatservice.domain.PresentationActivity;
import com.thecdmgroup.driiverseatservice.domain.ResponseObject;

/**
 * @author vinothn
 *
 */
public interface AnalyticService {

	
	/**
	 * @param query
	 * @return
	 * @throws ServiceException
	 */
	@RolesAllowed({ "REP" })
	List<ResponseObject> removeActivities(String query) throws ServiceException;
	
	/**
	 * 
	 * @param session
	 * @param appactivities
	 * @param presentationactivities
	 * @return
	 * @throws ServiceException
	 */
	
	@RolesAllowed({ "REP" })
	public ResponseObject postAnalytics(Analytics activities) throws ServiceException;

}
