/**
 * 
 */
package com.thecdmgroup.driiverseatservice.transport.jaxrs;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thecdmgroup.driiverseatservice.common.DriverSeatConstants;
import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.ResponseObject;
import com.thecdmgroup.driiverseatservice.domain.User;
import com.thecdmgroup.driiverseatservice.security.DriiverSeatUserPrincipal;
import com.thecdmgroup.driiverseatservice.service.UserService;

/**
 * @author vinothn
 *
 */
@Component
@Path("/session")
public class RESTSessionService {

	private static final Logger log = LogManager.getLogger(RESTSessionService.class);

	@Autowired
	private UserService userService;

	@Context
	private HttpServletRequest request = null;

	@Context
	private HttpServletResponse response = null;

	@Autowired
	private ServletContext context;

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public User login(Map<String, String> info) throws ServiceException {

		String user = info.get("user");
		String pass = info.get("pass");
		String env = info.get("env");
		log.debug("user: " + user + " pass" + pass);

		HttpSession session = request.getSession(true);
		// clear out any session stuff they may have had.
		clearSession(session);
		DriiverSeatUserPrincipal up = null;
		try {

			User us = userService.login(user, pass, env);
			if (us != null) {
				up = new DriiverSeatUserPrincipal(us.getFullName());
				up.setUser(us);
				session.setAttribute("principal", up);
				log.debug("Set principal on session: " + up);
				log.debug("REST Login Service, User: \n" + us.getUsername());
			}
			return us;
		} finally {
			context.setAttribute("principal", up);
			context.setAttribute("env", env);
			initializeLogContext(request);
		}
	}

	@GET
	@Path("getAssoicateHCPs")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> getAssoicateHCPs() throws ServiceException {

		log.debug("Start: getAssoicateHCPs: ");
		List<User> hcps = null;
		try {
			hcps = userService.getAssoicateHCPs();
			return hcps;
		} finally {
			log.debug("End: getAssoicateHCPs: ");
		}
	}

	@GET
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseObject logout() throws ServiceException {
		
		log.debug("Calling logout.  Request: " + request);
		ResponseObject response = new ResponseObject();
		userService.logout();
		HttpSession session = request.getSession();
		log.debug("Invalidating session " + session);

		if (session != null)
			session.invalidate();
		removeRememberLoginCookie();

		context.setAttribute("principal", null);
		context.setAttribute("env", null);
		response.setStatus(DriverSeatConstants.SUCCESS);
		return response;
	}

	////////////////////////////////////////////////////////////////////////////

	static final String REMEMBER_LOGIN_COOKIE = "remember.login";
	static final int REMEMBER_LOGIN_COOKIE_AGE = 24 * 60 * 60 * 365; // 1 year

	/**
	 * Implemented in the same way as
	 * 
	 * @param request
	 * @param userPrincipal
	 */
	private void initializeLogContext(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		// on Weblogic, the session Id's are very long and verbose:
		// SVWXJ4kVNqhpMldt1j3PnXCG2RtFLKZ6SjLMTBJn2k0TchyzbDny!-1366596253!1241015317245
		// that's too much to log, so we'll use the sessionid's hashcode instead
		// we only care about uniqueness anyway.
		String sessionIdHash = "" + Math.abs(sessionId.hashCode());
		// RequestID - just needs to be close to unique.
		// let's make it a hashcode of the current time+the sessionid.
		String ridkey = sessionId + System.currentTimeMillis();
		String rid = "" + Math.abs(ridkey.hashCode());

		ThreadContext.put(DriverSeatConstants.SESSION_ID, sessionIdHash);
		ThreadContext.put(DriverSeatConstants.REQUEST_ID, rid);
	}

	protected void clearSession(HttpSession ses) {
		Enumeration<String> keys = ses.getAttributeNames();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			Object val = ses.getAttribute(key);
			ses.removeAttribute(key);
			log.debug("Removed " + key + "=" + val);
		}
	}

	public void removeRememberLoginCookie() {
		Cookie rlc = new Cookie(REMEMBER_LOGIN_COOKIE, "");
		rlc.setMaxAge(0);
		log.debug("Setting Cookie: " + rlc);
		response.addCookie(rlc);
	}

}
