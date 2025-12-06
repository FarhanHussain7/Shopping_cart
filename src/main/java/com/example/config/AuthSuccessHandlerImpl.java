package com.example.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthSuccessHandlerImpl implements AuthenticationSuccessHandler {

//	This class is not used in securityConfig but if you want then uncomment the code and comment the other code 
//	This is just used to redirect after login 
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		Set<String> roles = AuthorityUtils.authorityListToSet(authorities);
		
		if(roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/");
		}
		else {
			response.sendRedirect("/");
		}
		
	}

}
