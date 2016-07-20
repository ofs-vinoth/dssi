package com.thecdmgroup.driiverseatservice.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.thecdmgroup.driiverseatservice.common.ServiceException;
import com.thecdmgroup.driiverseatservice.domain.User;
import com.thecdmgroup.driiverseatservice.security.DriiverSeatUserPrincipal;
import com.thecdmgroup.driiverseatservice.service.UserService;
import com.thecdmgroup.driiverseatservice.util.PropertyUtil;

import net.jmatrix.utils.PerfTrack;
import sun.misc.BASE64Decoder;

/**
 * 
 */
@Component
@SuppressWarnings("all")
public class RESTFilter implements Filter {
	static Logger log = LogManager.getLogger(RESTFilter.class);
	
	SecurityContext sc;

	@Autowired
	UserService userService;

	@Autowired
	PropertyUtil pUtil;

	/** */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("Initializing REST Access Filter");
		//sc = filterConfig.getServletContext();
		/*if (SecurityContextHolder.getContext() != null) {
			sc = SecurityContextHolder.getContext();
		} else {
			sc = SecurityContextHolder.createEmptyContext();
		}*/
		/*
		 * WebApplicationContext webApplicationContext =
		 * WebApplicationContextUtils.getWebApplicationContext(sc);
		 * 
		 * AutowireCapableBeanFactory autowireCapableBeanFactory =
		 * webApplicationContext.getAutowireCapableBeanFactory();
		 */
		try {
			// autowireCapableBeanFactory.configureBean(this, "RESTFilter");
			SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());
		} catch (Exception e) {
			System.out.println("e" + e);
		}

	}

	/** */
	@Override
	public void destroy() {
		log.debug("Destroying REST Access Filter.");
	}

	/** */
   @Override
   public void doFilter(ServletRequest request, ServletResponse response,
         FilterChain chain) throws IOException, ServletException {
      
	  HttpServletRequest hrequest=(HttpServletRequest)request;
      HttpServletResponse hresponse=(HttpServletResponse)response;
      sc = SecurityContextHolder.getContext();
      // this should really only be enabled in test.  This allows clients
      // from any server (globally) to access our REST based services.
      // This is necessary in Dev, where we may have a developer 
      // desktop creating UI, and services on a different server.  But in 
      // a typical production deployment, we'd have the html and REST
      // being served from the same domain - so cross-domain REST access
      // would not be an issue. 
      //
      // This has been added as an environment property, and enabled in 
      // all environment except prod.
      String pname="rest.disable.xdomain.security";
      if (pUtil != null && pUtil.getBooleanProperty(pname)) {
    	  log.info("Disabling REST cross-domain security because property "+
               pname + " is set to true.");
         
    	  String origin=hrequest.getHeader("origin");
    	  if (origin != null) {
    		  log.debug("Adding Header: Access-Control-Allow-Origin:"+origin);
    		  hresponse.addHeader("Access-Control-Allow-Origin", origin);
    	  } else {
    		  log.debug("Adding Header: Access-Control-Allow-Origin:*");
    		  hresponse.addHeader("Access-Control-Allow-Origin", "*");
    	  }
         
    	  log.debug("Adding Header: Access-Control-Allow-Credentials:true");
    	  hresponse.addHeader("Access-Control-Allow-Credentials", "true");
         
    	  String methods="GET,POST,PUT,DELETE,OPTIONS,HEAD";
    	  log.debug("Adding Header: Access-Control-Allow-Methods:"+methods);
    	  hresponse.addHeader("Access-Control-Allow-Methods", methods);
         
    	  String acrh=hrequest.getHeader("access-control-request-headers");
    	  if (acrh != null) {
    		  log.debug("Adding Header: Access-Control-Allow-Headers:"+acrh);
    		  hresponse.addHeader("Access-Control-Allow-Headers", acrh);
    	  }
         
//        log.debug("Adding header: Access-Control-Allow-Credentials: true");
//        hresponse.addHeader("Access-Control-Allow-Credentials", "true");
      }
      
      String userAgent=hrequest.getHeader("user-agent");
      if (userAgent == null)
         userAgent="null";
      
      // disable caching, otherwise REST method resopnses are cached
      // by URL - which is not nearly enough for uniqueness.  
      
      if (userAgent.contains("MSIE")) {
         String uri = hrequest.getRequestURI();
         if (uri == null)
            uri = "";

         log.debug("Browser is IE. Sending a whole mess of cache-control headers that make sure AJAX calls are not cached.");
         // Got these from here:
         // http://vegdave.wordpress.com/2007/11/08/cool-ajax-trick-for-the-day-to-solve-ie-caching-issue/#comment-13678
         // It works.
         hresponse.setDateHeader("Expires", -1);
         hresponse.setHeader("Cache-Control", "private");
         hresponse.setHeader("Last-Modified", new Date().toString());
         hresponse.setHeader("Pragma", "no-cache");
         hresponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
      } else {
         hresponse.addHeader("Cache-Control", "no-cache");
      }
      
      HttpSession session=hrequest.getSession(false);
      
      String realm="CDM";
      
      String auth=hrequest.getHeader("Authorization");
      log.debug("Basic Auth: " + auth);
      
      String perf="REST("+ hrequest.getMethod()+" "+hrequest.getRequestURI()+")";
      try { // try block for PerfTrack().
         PerfTrack.start(perf);
    	 
         // something like this
         // Basic YnmZV0dpnZXJYW5NQ==
         if (auth != null) {
            if (auth.indexOf(" ") != -1)
               auth=auth.split("\\ ")[1];
            
            BASE64Decoder decoder=new BASE64Decoder();
            
            auth=new String(decoder.decodeBuffer(auth));
            
            log.debug("Auth decoded: "+auth);
            
            try {
               ThreadContext.put("TRANSPORT", "REST");
               String components[]=auth.split("\\:");
               String username=components[0];
               String pass=components[1];
               String env=components[2];
               
               User us = userService.login(username, pass, env);
               DriiverSeatUserPrincipal up = new DriiverSeatUserPrincipal(us.getUsername());
               log.debug("Principal: "+up);
               Authentication authentication = new UsernamePasswordAuthenticationToken(up, null);
               sc.setAuthentication(authentication);
               SecurityContextHolder.setContext(sc);
               chain.doFilter(request, response);
            } catch (SecurityException ex) {
               log.debug("SEcurity Exception: "+ex);
               throw new ServletException("Error authorizing", ex);
            } catch (Exception ex) {
               log.debug("Exception: ", ex);
               throw new ServletException("Error authorizing", ex);
            } finally {
               sc.setAuthentication(null);
               ThreadContext.remove("TRANSPORT");
            }
         } else if (session != null && session.getAttribute("principal") != null) {
            log.debug("REST Using existing HTTP Session principal for Auth.");
            
            try {
               ThreadContext.put("TRANSPORT", "REST");
               DriiverSeatUserPrincipal principal = (DriiverSeatUserPrincipal)session.getAttribute("principal");
               log.debug("Principal: "+principal);
               Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null);
               sc.setAuthentication(authentication);
               chain.doFilter(request, response);
            } finally {
               log.debug("Finally - removing session user.");
               sc.setAuthentication(null);
               ThreadContext.remove("TRANSPORT");
            }
         } else {
            try {
               ThreadContext.put("TRANSPORT", "REST");
               log.debug("No auth info in headers, proceeding un-authenticated.");
               
               chain.doFilter(request, response);
            } catch (Exception ex) {
               log.debug("Exception: ", ex);
               throw ex;
            } finally {
            	ThreadContext.remove("TRANSPORT");
            }
         }
      } finally {
         PerfTrack.stop(perf);
         if (PerfTrack.isCurrentRootAndComplete()) {
            log.info("\n"+PerfTrack.toString(1));
            PerfTrack.clear();
         }
         
         // this is a failsafe. 
         sc.setAuthentication(null);
      }
   }
}
