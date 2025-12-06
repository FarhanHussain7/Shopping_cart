package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.model.UserDtls;
import com.example.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		UserDtls user = userRepository.findByEmail(username);
		
		if(user==null) {
			throw new UsernameNotFoundException("user not found");
		}
		return org.springframework.security.core.userdetails.User
			    .withUsername(user.getEmail())
			    .password(user.getPassword())
			    .authorities(new SimpleGrantedAuthority(user.getRole())) // "ROLE_ADMIN"
			    .build();
	}
}
