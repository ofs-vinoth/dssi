/**
 * 
 */
package com.thecdmgroup.driiverseatservice.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author vinothn
 *
 */
public class DriiverSeatAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LogManager.getLogger(DriiverSeatAuthenticationProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) {

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = String.valueOf(auth.getPrincipal());
		String password = String.valueOf(auth.getCredentials());

		logger.info("username:" + username);
		logger.info("password:" + password);
		/* what should happen here? */

		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		// AS OF NOW THERE IS NO ROLE 'HCP'. SO WE HARD CODED WITH REP
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_REP"));
		Authentication authe = new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
		return authe;
	}

	@Override
	public boolean supports(Class aClass) {
		return true; // To indicate that this authenticationprovider can handle
						// the auth request. since there's currently only one
						// way of logging in, always return true
	}

}