/**
 * 
 */
package com.thecdmgroup.driiverseatservice.transport.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.Analytics;
import com.thecdmgroup.driiverseatservice.domain.ResponseObject;
import com.thecdmgroup.driiverseatservice.domain.VeevaiDetailMetaData;
import com.thecdmgroup.driiverseatservice.service.AnalyticService;
import com.thecdmgroup.driiverseatservice.service.VeevaiDetailService;

/**
 * @author vinothn
 *
 */
@Component
@Path("/analytics")
public class RESTAnalyticService {

	private static final Logger log = LogManager.getLogger(RESTAnalyticService.class);

	@Autowired
	private AnalyticService analticService;
	
	@Autowired
	private VeevaiDetailService veevaiDetailService;

	@Context
	private HttpServletRequest request = null;

	@Context
	private HttpServletResponse response = null;

	@Autowired
	private ServletContext context;

	/**
	 * 
	 * Dummy Dev Service - Should not use by application
	 * @param queries
	 * @return
	 * @throws ServiceException
	 */
	@POST
	@Path("/removeActivies")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ResponseObject> removeActivies(List<String> queries) throws ServiceException {

		log.debug("Start: removeActivies: ");
		List<ResponseObject> result = new ArrayList<ResponseObject>();
		try {
			for (String query : queries ) {
				List<ResponseObject> list = analticService.removeActivities(query);
				result.addAll(list);
			}
		} finally {
			log.debug("End: removeActivies: " + result);
		}
		return result;
	}
	
	
	@POST
	@Path("/activities")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseObject postAnalytics (Analytics activities)
			throws ServiceException {
		
		log.debug("Start: postAnalyticsActivities: ");
		ResponseObject result = null;
		try {
			result = analticService.postAnalytics(activities);
		} finally {
			log.debug("End: postAnalyticsActivities: " + result);
		}
		return result;
	}
	
	@GET
	@Path("/VeevaiDetailMetaData")
	@Produces(MediaType.APPLICATION_JSON)
	public VeevaiDetailMetaData getVeevaiDetailMetaData (@QueryParam("iDetatilName") String iDetatilName)
			throws ServiceException {
		
		log.debug("Start: postAnalyticsActivities: ");
		VeevaiDetailMetaData result = null;
		try {
			result = veevaiDetailService.getVeevaiDetailMetaData(iDetatilName);
		} finally {
			log.debug("End: postAnalyticsActivities: " + result);
		}
		return result;
	}

}
